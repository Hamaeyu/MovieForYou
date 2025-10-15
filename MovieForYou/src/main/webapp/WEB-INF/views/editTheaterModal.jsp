<%@ page contentType="text/html; charset=UTF-8" %>
<div class="modal fade" id="editTheaterModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content bg-dark text-light rounded-3 border-secondary shadow">
      <div class="modal-header border-secondary">
        <h5 class="modal-title"><i class="bi bi-pencil-square text-warning"></i> 영화관 정보 수정</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
      </div>

      <div class="modal-body">
        <form id="editTheaterForm" method="post" action="<%= request.getContextPath() %>/admin/updateCinema">
          <!-- 수정용 hidden ID -->
          <input type="hidden" name="id">

          <div class="mb-3">
            <label class="form-label">Cinema Name *</label>
            <input type="text" class="form-control bg-dark text-light border-secondary" name="cinemaName" required>
          </div>

          <div class="row">
            <div class="col-md-4 mb-3">
              <label class="form-label">Address *</label>
              <input type="text" class="form-control bg-dark text-light border-secondary" name="address" required>
            </div>
            <div class="col-md-4 mb-3">
              <label class="form-label">Brand *</label>
              <select class="form-select bg-dark text-light border-secondary" name="type" required>
                <option value="">Select type</option>
                <option value="1">CGV</option>
                <option value="2">Megabox</option>
                <option value="3">롯데시네마</option>
                <option value="4">기타</option>
              </select>
            </div>
            <div class="col-md-4 mb-3">
              <label class="form-label">Region *</label>
              <select class="form-select bg-dark text-light border-secondary" name="region" required>
                <option value="">지역을 선택해주세요</option>
                <option value="1">서울</option>
                <option value="2">부산</option>
                <option value="3">경기</option>
              </select>
            </div>
          </div>

          <div class="row">
            <div class="col-md-6 mb-3">
              <label class="form-label">Latitude *</label>
              <input type="text" class="form-control bg-dark text-light border-secondary" name="latitude" required>
            </div>
            <div class="col-md-6 mb-3">
              <label class="form-label">Longitude *</label>
              <input type="text" class="form-control bg-dark text-light border-secondary" name="longitude" required>
            </div>
          </div>
        </form>
      </div>

      <div class="modal-footer border-secondary">
        <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button class="btn btn-success" type="submit" form="editTheaterForm">수정하기</button>
      </div>
    </div>
  </div>
</div>

<script>
document.addEventListener("DOMContentLoaded", function() {
  // ✏️ 수정 버튼 클릭 시 새 모달 오픈
  document.querySelector("#theater-container").addEventListener("click", function(e) {
    const btn = e.target.closest(".edit-btn");
    if (!btn) return;

    const modal = document.querySelector("#editTheaterModal");
    const form = document.querySelector("#editTheaterForm");

    // ✅ dataset 값 입력
    form.querySelector("[name='id']").value = btn.dataset.id;
    form.querySelector("[name='cinemaName']").value = btn.dataset.name;
    form.querySelector("[name='address']").value = btn.dataset.address;
    form.querySelector("[name='latitude']").value = btn.dataset.lat;
    form.querySelector("[name='longitude']").value = btn.dataset.lng;
    form.querySelector("[name='type']").value = btn.dataset.type;
    form.querySelector("[name='region']").value = btn.dataset.region;

    // ✅ 모달 표시
    const modalInstance = new bootstrap.Modal(modal);
    modalInstance.show();
  });
});
</script>
