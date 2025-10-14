<%@ page contentType="text/html; charset=UTF-8" %>
<div class="modal fade" id="theaterModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content bg-dark text-light rounded-3 border-secondary shadow">
      <div class="modal-header border-secondary">
        <h5 class="modal-title"><i class="bi bi-geo-alt-fill text-warning"></i> Theatre Management</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
      </div>

      <div class="modal-body">
        <form id="theaterForm" method="post" action="cinemaInsert">
          <div class="mb-3">
            <label class="form-label">Cinema Name *</label>
            <input type="text" class="form-control bg-dark text-light border-secondary" name="cinemaName" placeholder="Enter theatre name..." required>
          </div>

          <div class="row">
            <div class="col-md-4 mb-3">
              <label class="form-label">Address *</label>
              <input type="text" class="form-control bg-dark text-light border-secondary" name="address" placeholder="Enter complete address..." required>
            </div>
            <div class="col-md-4 mb-3">
              <label class="form-label">Brand *</label>
              <select class="form-select bg-dark text-light border-secondary" name="type">
                <option value="">Select type</option>
                <option value="1">CGV</option>
                <option value="2">Megabox</option>
                <option value="3">롯데시네마</option>
                <option value="4">기타</option>
              </select>
            </div>
            <div class="col-md-4 mb-3">
              <label class="form-label">지역 *</label>
              <select class="form-select bg-dark text-light border-secondary" name="region">
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
              <div class="input-group">
                <input type="text" class="form-control bg-dark text-light border-secondary" name="latitude" value="37.7749" required>
                <button class="btn btn-outline-success" type="button"><i class="bi bi-geo-alt"></i></button>
              </div>
            </div>
            <div class="col-md-6 mb-3">
              <label class="form-label">Longitude *</label>
              <div class="input-group">
                <input type="text" class="form-control bg-dark text-light border-secondary" name="longitude" value="122.4194" required>
                <button class="btn btn-outline-info" type="button">Auto</button>
              </div>
            </div>
          </div>

          <div class="border-top border-secondary pt-3">
		    <h6 class="text-warning"><i class="bi bi-pin-map-fill"></i> Map Preview</h6>
		    <div id="map" style="width:100%;height:300px;border-radius:10px;"></div>
		  </div>
      </div>

      <div class="modal-footer border-secondary">
        <span class="me-auto small">Total: 3 theatres • <span class="text-success">Active: 2</span> • <span class="text-danger">Inactive: 1</span></span>
        <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button class="btn btn-outline-warning" type="reset" form="theaterForm">Clear</button>
        <button class="btn btn-success" type="submit" form="theaterForm">Save Theatre</button>
      </div>
      </form>
    </div>
  </div>
</div>
<script type="text/javascript" src="https://oapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=0p5xjv38v6&submodules=geocoder"></script>
<script>
let map, marker;

document.getElementById('theaterModal').addEventListener('shown.bs.modal', () => {
	if (!map) {
	 map = new naver.maps.Map('map', {
	   center: new naver.maps.LatLng(37.5665, 126.9780), // 초기: 서울 중심
	   zoom: 14,
	 });
	 marker = new naver.maps.Marker({
	   position: map.getCenter(),
	   map: map
	 });
	}
	});
	
	const addressInput = document.querySelector('input[name="address"]');
	addressInput.addEventListener('change', function() {
	const address = this.value.trim();
	if (!address) return;
	
	naver.maps.Service.geocode({ query: address }, function(status, response) {
	 if (status !== naver.maps.Service.Status.OK) {
	   alert('주소를 찾을 수 없습니다.');
	   return;
	 }
	
	 const item = response.v2.addresses[0];
	 const lat = parseFloat(item.y);
	 const lng = parseFloat(item.x);
	
	 document.querySelector('input[name="latitude"]').value = lat;
	 document.querySelector('input[name="longitude"]').value = lng;
	
	 // 지도 이동 및 마커 표시
	 const newPos = new naver.maps.LatLng(lat, lng);
	 map.setCenter(newPos);
	 marker.setPosition(newPos);
	});
});
	

	
document.addEventListener("DOMContentLoaded", function () {

	  // ✏️ 수정 버튼 클릭 이벤트 위임
	  document.querySelector("#theater-container").addEventListener("click", function(e) {
	    const btn = e.target.closest(".edit-btn");
	    if (!btn) return;
	    // ✅ 기존 등록 모달 재활용
	    const modal = document.querySelector("#theaterModal");
	    const modalTitle = modal.querySelector(".modal-title");
	    const submitBtn = modal.querySelector(".btn-submit"); // 등록 버튼
	    const form = modal.querySelector("form");

	    // 모달 제목, 버튼 텍스트 수정
	    modalTitle.textContent = "영화관 정보 수정";
	    submitBtn.textContent = "수정하기";

	    // ✅ 입력 필드 채우기
	    form.querySelector("[name='id']").value = btn.dataset.id;
	    form.querySelector("[name='name']").value = btn.dataset.name;
	    form.querySelector("[name='address']").value = btn.dataset.address;
	    form.querySelector("[name='latitude']").value = btn.dataset.lat;
	    form.querySelector("[name='longitude']").value = btn.dataset.lng;
	    form.querySelector("[name='type']").value = btn.dataset.type;
	    form.querySelector("[name='region']").value = btn.dataset.region;

	    // ✅ 폼 액션 수정
	    form.action = "<%= request.getContextPath() %>/admin/updateCinema";

    // ✅ 모달 띄우기
    const modalInstance = new bootstrap.Modal(modal);
    modalInstance.show();
  });

});
</script>

