<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach var="cinema" items="${cinemaList}">
  <div class="user">
    <div class="user-info">
      <div class="user-avatar green">J</div>
      <div>
        <strong>${cinema.name}</strong><br>
        <span class="user-email">${cinema.address}</span><br>
        <small>${cinema.type} · ${cinema.region}</small>
      </div>
    </div>

    <div class="user-actions">
      <button 
	  type="button"
	  class="btn btn-sm btn-warning edit-btn"
	  data-id="${cinema.theaterId}"
	  data-name="${cinema.name}"
	  data-address="${cinema.address}"
	  data-lat="${cinema.lat}"
	  data-lng="${cinema.lng}"
	  data-type="${cinema.typeId}"
	  data-region="${cinema.regionId}">
	  ✏️
	</button>
      <button class="btn btn-sm btn-danger delete-btn" data-id="${cinema.theaterId}">🗑️</button>
    </div>
  </div>
</c:forEach>

