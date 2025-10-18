/**
 * freeWrite.jsp에 포함
 * CKEditor5 + 임시 이미지 업로드
 */
let editorInstance = null; // 전역 변수로 선언
// 업로드된 파일 관리용
const uploadedFiles = []; //  [{ fileId(임시이미지PK), default: 이미지URL, tempUuid }]
// 글 작성 시작 시 1번만 생성
const currentTempUuid = generateSecureUuid(); // 글 단위 UUID

let isUploading = false; // 업로드 상태 플래그
/*
추후 
CKEditor의 FileRepository 또는 ImageUploadEditing 플러그인을 오버라이드해서
“업로드 완료”와 “이미지 삭제” 시점을 명시적으로 감지하게 바꾸기
 */

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
    isUploading = true;// 업로드 시작 시 플래그 on
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
            fileId: data.id,        // DB temp_post_image PK
            default: data.imageUrl, // 오브젝트 스토리지 URL
            tempUuid: tempUuid       // 같은 세션 업로드 UUID
        });
        
        uploadedFilesCount++;
        console.log('업로드 성공, 저장 완료:', data.id);
        isUploading = false; //업로드 완료 후 플래그 off
        //CKEditor 업로드 어댑터가 요구하는 리턴값
        // editor.plugins.get('FileRepository').createUploadAdapter 내에서 사용됨
        return {
            default: data.imageUrl, // 에디터 내부에서 이미지 표시용 URL
            fileId: data.id          // 삭제/마이그레이션용 ID
        }; 
    })
    .catch(err => {
        isUploading = false; //업로드 완료 후 플래그 off
        //uploadedFilesCount = Math.max(0, uploadedFilesCount - 1); // 0보다 내려가지 않도록 안전하게 // 에러 시 카운트 감소
        console.error('업로드 실패:', err);
        throw err;// CKEditor 어댑터로 다시 전달 → 에디터에 실패 표시
    });
}

// CKEditor에 커스텀 업로드 어댑터 등록용 플러그인 함수
function MyCustomUploadAdapterPlugin(editor) {
    editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
        //정의한 uploadFileAsync() 함수 사용
        return {
            upload: () =>
                loader.file.then(file =>
                    uploadFileAsync(file, currentTempUuid)
                        .then(fileInfo => ({ default: fileInfo.default }))
                ),
            abort: () => {
                console.log('업로드 중단됨');
            }
        };
    };
}

// 파일 삭제 (서블릿용 POST 삭제)
function deleteFile(fileId) {
    if (!fileId) return;

    fetch('/freeboard/delete-image', { //삭제요청 엔드포인트
        method: 'POST', // 서블릿은 DELETE 못 쓰니까 POST로 처리
        body: JSON.stringify({ id: fileId }) //요청에 바디에 보냄
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
        extraPlugins: [MyCustomUploadAdapterPlugin], // 사용자 업로드 어댑터
        ckfinder: { uploadUrl: '/freeboard/upload-image' } // 기본 업로드 URL
    })
    .then(editor => {
        editorInstance = editor;
        const editable = editor.ui.view.editable.element; // 바깥에서 한 번만 정의
        // 에디터 스타일 적용
        const applyEditorStyle = () => {
            editable.style.backgroundColor = '#ffffff';
            editable.style.color = '#000000';
            editable.style.minHeight = '400px';
            editable.style.padding = '1rem';
            editable.style.borderRadius = '8px';
        };
        applyEditorStyle();
        editor.editing.view.document.on('change:isFocused', applyEditorStyle);

        // 업로드 어댑터 정의
        // CKEditor5 업로드 어댑터 정의
        editor.plugins.get('FileRepository').createUploadAdapter = loader => ({
            upload: () => loader.file.then(file => uploadFileAsync(file, currentTempUuid)
                .then(fileInfo => ({ default: fileInfo.default }))
            )
        });
        
        // --- 이미지 삭제 감지 ---
           const observer = new MutationObserver(mutations => {
               if (isUploading) return;

               mutations.forEach(mutation => {
                   mutation.removedNodes.forEach(node => {
                       // figure 태그 안에 이미지 있는 경우 처리
                       if (node.nodeName === 'FIGURE') {
                           const img = node.querySelector('img');
                           if (img) {
                               const src = img.getAttribute('src');
                               const file = uploadedFiles.find(f => f.default === src);
                               if (file) {
                                   console.log('DOM 감지 → 이미지 삭제됨:', file.fileId);
                                   deleteFile(file.fileId);
                               }
                           }
                       }
                   });
               });
           });

           // 실제 DOM 요소 관찰
           observer.observe(editable, { childList: true, subtree: true });
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

//글 등록 처리 - post
document.getElementById('btn-submit').addEventListener('click', function(e) {
    //클릭이벤트
    e.preventDefault(); // form submit 기본 동작 막기
    
    if (!editorInstance) {
        alert('에디터가 준비되지 않았습니다. 잠시 후 다시 시도해주세요.');
        return;
    }

    //업로드 중이면 등록 불가
    if (isUploading) {
        alert('이미지 업로드가 완료될 때까지 기다려주세요.');
        return;
    }

    //제목, 내용 검증
    const title = document.getElementById('title').value.trim();
    const content = editorInstance.getData().trim();
    if (!title || !content) {
        alert('제목 또는 내용을 입력해주세요.');
        return;
    }

    // content: editorInstance.getData() 사용
    const editorContent = editorInstance.getData();

    // DOMParser를 이용해 content를 파싱
    const parser = new DOMParser();
    const doc = parser.parseFromString(editorContent, 'text/html');

    // 에디터에 남아있는 이미지 src 추출
    const imgSrcs = Array.from(doc.querySelectorAll('img')).map(img => img.getAttribute('src'));

    // uploadedFiles 배열에서 실제 존재하는 이미지 ID만 필터링
    const tempImages = uploadedFiles
        .filter(f => imgSrcs.includes(f.default)) 
        .map(f => ({ fileId: f.fileId, url: f.default, tempUuid: f.tempUuid }));

    const payload = {
        title: title,
        content: content,
        tempImages
    };
    console.log(payload);
    fetch('/createok.free', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(payload)
    })
    .then(res => res.json())
    .then(res => {
        if (res.success) {
            //alert('글 등록 성공!');
            window.location.href = '/list.free'; //-> 리다이렉트 url
        } else {
            alert('글 등록 실패: ' + res.message);
        }
    })
    .catch(err => {
        console.error('요청 실패:', err);
        alert('서버 요청 실패');
    });
});