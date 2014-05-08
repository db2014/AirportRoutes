$(document).ready(function (){

	function initialize() {
	  var mapOptions = {
	    zoom: 3,
	    center: new google.maps.LatLng(0, 0),
	    mapTypeId: google.maps.MapTypeId.TERRAIN
	  };

	  var map = new google.maps.Map(document.getElementById('map-canvas'),
	      mapOptions);


	}

	google.maps.event.addDomListener(window, 'load', initialize);
	
	$("#callAjax").click(function (){	
		//alert("asd");
		$.ajax({
			url:'/callDajkst',
			type:'POST',
			data:{
				odA:$("#sTO").val(),
				doB:$("#sFROM").val(),
				txtRange:$("#txtRange").val()
				
			},
			success:function(data){
				alert(data);
				
			},
			error:function(data){				
				alert("GRESKA");				
			}
		});//ajax
	});//button
	
	
});//document ready