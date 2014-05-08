$(document).ready(function (){
	var map;
	var zoom=4;
	var lat=0;
	var long=0;
	
	function initialize() {
		  var mapOptions = {					  
		    zoom: zoom,
		    center: new google.maps.LatLng(lat, long),
		    mapTypeId: google.maps.MapTypeId.TERRAIN
		  };

		
		   map = new google.maps.Map(document.getElementById('map-canvas'),
			      mapOptions);		
		 
		}

	google.maps.event.addDomListener(window, 'load', initialize);
	
	$("#callAjax").click(function (){	
		$.ajax({
			url:'/callDajkst',
			type:'POST',
			data:{
				odA:$("#sFROM").val(),
				doB:$("#sTO").val(),
				txtRange:$("#txtRange").val()
				
			},
			success:function(data){
				alert(data);
				zoom=4;
				lat=0;
				long=-45;				
				initialize();
				
				var flightPlanCoordinates = [];
				
				for(var i=0;i<data.length;i++){
					var s=data[i]+"";
					var a=s.split('*');	
					flightPlanCoordinates[i]=new google.maps.LatLng(a[1],a[0]);
						}
			
				var flightPath = new google.maps.Polyline({
                  		    path: flightPlanCoordinates,
                  		    geodesic: true,
                  		    strokeColor: '#FF0000',
                  		    strokeOpacity: 1.0,
                  		    strokeWeight: 2
                  		  });				                  		  
				                  		
				flightPath.setMap(map);
	
			},
			error:function(data){				
				alert("GRESKA");
			
			}
		});
	});
	
	
});//document ready