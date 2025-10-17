const postCards = document.querySelectorAll('.post-card');
postCards.forEach(card => {
    card.addEventListener('click', () => {
        const postId = card.dataset.id; // data-id 값 가져오기
        window.location.href = `/detail.free?id=${postId}`; // 상세 페이지로 이동
    });
});