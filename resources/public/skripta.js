//$body = $("body");

/*
$(document).on({
    ajaxStart: function() { $body.addClass("loading");    },
     ajaxStop: function() { $body.removeClass("loading"); }    
});

*/

$(document).ready(function (){
	

	
	
	$("#sFROM").select2({
		
		    minimumInputLength: 3
	});
	$("#sTO").select2({
		 
		    minimumInputLength: 3
		
		
	});
	var map;
	var zoom=2;
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
		//alert($("#od").val());
		 $("body").addClass("loading");
		
		$.ajax({
			url:'/callDajkst',
			type:'POST',
			data:{
				odA:$("#sFROM").val(),
				doB:$("#sTO").val(),
				txtRange:$("#txtRange").val()
				
			},
			success:function(data){
				var m=data[data.length-1]+"";
				mxy=m.split('*');
				
				if(data.length>2){
				zoom=4;
				lat=mxy[0];
				long=mxy[1];
			}
				
				initialize();
				 $("body").removeClass("loading");
			
				
				var flightPlanCoordinates = [];
			
				var ruta="<h4>";
				for(var i=0;i<data.length-1;i++){
					
					var s=data[i]+"";
					var a=s.split('*');	
			
					ruta =ruta + a[2] + "->";
					
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
				ruta=ruta.slice(0,-2);
				ruta+='</h4>';
			
				if(data.length<=2){
					ruta="<h3>buy airplane with a greater range!</h3>";
				}
					$("#routeList").html(ruta);
					
				
			},
			error:function(data){
				
				alert("GRESKA");
				 $("body").removeClass("loading");
			}
		});
		
	});
	
	
});//document ready