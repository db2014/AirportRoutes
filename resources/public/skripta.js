$(document).ready(function (){
	$("#callAjax").click(function (){	
		$.ajax({
			url:'/testAjax',
			type:'POST',
			data:{
				a:"parametar 1",
				b:"parametar 2"
				
			},
			success:function(data){
				alert("HELLO FROM AJAX");				
			},
			error:function(){
				alert("ERROR");
			}
		});
	});
});