$(function(){
  // Summernote
  $('#summernote').summernote({
    height:350, lang:'ko-KR',
    placeholder:'상세 후기를 입력하세요...',
    toolbar:[
      ['style',['bold','italic','underline','clear']],
      ['font',['fontsize','color']],
      ['para',['ul','ol','paragraph']],
      ['insert',['picture','link','video']],
      ['view',['fullscreen','codeview']]
    ],
    callbacks:{
      onImageUpload:function(files){
        const fd=new FormData();
        fd.append("file",files[0]);
        $.ajax({
          url: APP_CTX+"/uploadImage.cinema",
          method:"POST",
          data:fd,
          processData:false,
          contentType:false,
          success:function(res){
            $('#summernote').summernote('insertImage',res.url);
          },
          error:function(){ alert("이미지 업로드 실패"); }
        });
      }
    }
  });

  // 브랜드 → 지역 → 상영관 비동기 연결
  $.getJSON(APP_CTX+"/brands.cinema",function(brands){
    $("#brandSel").append('<option value="">브랜드 선택</option>');
    $.each(brands,function(_,b){ $("#brandSel").append(`<option value='${b.id}'>${b.name}</option>`); });
  });

  $("#brandSel").on("change",function(){
    const brandId=$(this).val();
    $("#regionSel").empty().prop("disabled",!brandId);
    $("#cinemaSel").empty().prop("disabled",true);
    if(!brandId)return;
    $.getJSON(APP_CTX+"/regions.cinema",{brandId},function(regions){
      $("#regionSel").append('<option value="">지역 선택</option>');
      $.each(regions,function(_,r){ $("#regionSel").append(`<option value='${r.id}'>${r.name}</option>`); });
    });
  });

  $("#regionSel").on("change",function(){
    const brandId=$("#brandSel").val();
    const regionId=$(this).val();
    $("#cinemaSel").empty().prop("disabled",!regionId);
    if(!regionId)return;
    $.getJSON(APP_CTX+"/cinemas.cinema",{brandId,regionId},function(cs){
      $("#cinemaSel").append('<option value="">상영관 선택</option>');
      $.each(cs,function(_,c){ $("#cinemaSel").append(`<option value='${c.id}'>${c.name}</option>`); });
    });
  });
});
