/**
 * freeWrite.jsp에 포함
 * CKEditor5 + 임시 이미지 업로드
 */

// 업로드된 파일 관리용
const uploadedFiles = []; //  [{ fileId(임시이미지PK), default: 이미지URL, tempUuid }]
// 글 작성 시작 시 1번만 생성
const currentTempUuid = generateSecureUuid(); // 글 단위 UUID

// 업로드 제한
const MAX_FILES = 5;
let uploadedFilesCount = 0;

// 파일 검사 함수
function validateFile(file) {
    const MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    const ALLOWED_TYPES = ['image/png', 'image/jpeg', 'image/gif'];

    if (!ALLOWED_TYPES.includes(file.type)) {
        alert('허용되지 않는 파일 형식입니다.');
        return false;
    }

    if (file.size > MAX_FILE_SIZE) {
        alert('파일 크기가 10MB를 초과했습니다.');
        return false;
    }

    return true; // 조건 통과
}

/**
 * 비동기 업로드 함수
 * @param {File} file - 업로드할 파일
 * @param {string} tempUuid - 동일 세션 업로드 구분 UUID
 * @returns {Promise<{default: string, id: number}>} 
 *   - CKEditor가 요구하는 업로드 응답 형식
 *   - default: 에디터에서 표시할 이미지 URL
 *   - fileId: 서버에서 발급한 임시 이미지 ID, 삭제/마이그레이션용
 */
function uploadFileAsync(file, tempUuid) {
    if (uploadedFilesCount >= MAX_FILES) {
        alert(`최대 ${MAX_FILES}개까지 업로드 가능합니다.`);
        return Promise.reject('파일 수 초과'); //Promise에서 에러 상태로 반환한다는 뜻
    }

    if (!validateFile(file)) {
        return Promise.reject('파일 유효성 실패'); //Promise에서 에러 상태로 반환한다는 뜻
    }
    
    // 비동기 요청 시 보낼 데이터
    const formData = new FormData();
    formData.append('upload', file);
    formData.append('tempUuid', tempUuid);

    return fetch('/freeboard/upload-image', { //비동기 요청 url
        method: 'POST',
        body: formData //요청 바디에 보냄
    })
    .then(res => {
            if (!res.ok) { // HTTP 상태코드 확인
                throw new Error(`HTTP 오류: ${res.status}`);
            }
            return res.json();
        }) // 서버에서 응답으로 임시 이미지 식별자(id), imageUrl, success여부, message 넘겨주어야 함
    .then(data => {
        if (!data.success) {
           throw new Error(data.message);
        }
        
        // 성공 시 uploadedFiles 배열에 저장
        uploadedFiles.push({
            fileId: data.id,            // DB temp_post_image PK
            default: data.imageUrl, // 오브젝트 스토리지 URL
            tempUuid: tempUuid       // 같은 세션 업로드 UUID
        });
        
        uploadedFilesCount++;
        console.log('업로드 성공, 저장 완료:', data.id);
        
        //CKEditor 업로드 어댑터가 요구하는 리턴값
        // editor.plugins.get('FileRepository').createUploadAdapter 내에서 사용됨
        return {
            default: data.imageUrl, // 에디터 내부에서 이미지 표시용 URL
            fileId: data.id          // 삭제/마이그레이션용 ID
        }; 
    })
    .catch(err => {
        //uploadedFilesCount = Math.max(0, uploadedFilesCount - 1); // 0보다 내려가지 않도록 안전하게 // 에러 시 카운트 감소
        console.error('업로드 실패:', err);
        throw err;// CKEditor 어댑터로 다시 전달 → 에디터에 실패 표시
    });
}

// 파일 삭제 (서블릿용 POST 삭제)
function deleteFile(fileId) {
    if (!fileId) return;
    //요청에 보낼 데이터 생성 
    const formData = new FormData();
    formData.append('id', fileId);

    fetch('/freeboard/delete-image', { //삭제요청 엔드포인트
        method: 'POST', // 서블릿은 DELETE 못 쓰니까 POST로 처리
        body: formData //요청에 바디에 보냄
    })
    .then(res => {
        if (!res.ok) throw new Error(`HTTP 오류: ${res.status}`); //fetch쓰면 수동으로 해줘야함
        return res.json();
    })
    .then(data => { //data는 첫번째 then의 리턴값(res.json())
        if (!data.success) throw new Error(data.message);
        console.log('삭제 성공:', fileId);
        uploadedFilesCount = Math.max(0, uploadedFilesCount - 1); // 0보다 내려가지 않도록 안전하게
        
        // 배열에서 제거
        const idx = uploadedFiles.findIndex(f => f.fileId === fileId); //배열에서 조건을 만족하는 첫 번째 요소의 인덱스를 반환
        //findIndex가 조건에 맞는 요소를 찾으면 인덱스를 반환하고, 없으면 -1 반환
        if (idx > -1) uploadedFiles.splice(idx, 1);//시작 인덱스, 삭제할 요소 개수(1개)
        
    })
    .catch(err => console.error('삭제 실패:', err));
}


// CKEditor5 초기화
ClassicEditor
    .create(document.querySelector('#editor'), {
        ckfinder: { uploadUrl: '/freeboard/upload-image' } // 기본 업로드 URL
    })
    .then(editor => {
        // 에디터 스타일 적용
        const applyEditorStyle = () => {
            const editable = editor.ui.view.editable.element;
            editable.style.backgroundColor = '#ffffff';
            editable.style.color = '#000000';
            editable.style.minHeight = '400px';
            editable.style.padding = '1rem';
            editable.style.borderRadius = '8px';
        };
        applyEditorStyle();
        editor.editing.view.document.on('change:isFocused', applyEditorStyle);

        // 업로드 어댑터 정의
        editor.plugins.get('FileRepository').createUploadAdapter = loader => {
            return {
                upload: () => loader.file.then(file => {
                    // 파일마다 새 UUID X, 글 단위 UUID 사용
                    return uploadFileAsync(file, currentTempUuid)
                        .then(fileInfo => ({
                            default: fileInfo.default
                        }));
                })
            };
        };
        
        // 이미지 삭제 감지
        editor.model.document.on('change:data', () => {
            const currentImages = [];
            for (const node of editor.model.document.getRoot().getChildren({ includeAll: true })) {
                if (node.name === 'image') currentImages.push(node.getAttribute('src'));
            }

            // uploadedFiles 배열과 비교 후 삭제
            uploadedFiles.slice().forEach(file => {
                if (!currentImages.includes(file.default)) deleteFile(file.fileId);
            });
        });
    })
    .catch(error => console.error(error));

// UUID 생성
function generateSecureUuid() {
    // 16바이트(128비트) 배열 생성
    const array = new Uint8Array(16);
    crypto.getRandomValues(array); // 안전한 난수 생성

    // RFC4122 v4 규격 적용
    array[6] = (array[6] & 0x0f) | 0x40; // version 4
    array[8] = (array[8] & 0x3f) | 0x80; // variant

    // 16진수 문자열로 변환
    const hex = [...array].map(b => b.toString(16).padStart(2, '0')).join('');

    // UUID 포맷: 8-4-4-4-12
    return `${hex.substr(0, 8)}-${hex.substr(8, 4)}-${hex.substr(12, 4)}-${hex.substr(16, 4)}-${hex.substr(20)}`;
}
