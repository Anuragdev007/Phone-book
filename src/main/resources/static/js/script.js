
const toggleSidebar=()=>{
	
	if($(".sidebar").is(":visible")){
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	}else{
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
		
	}
	
}


const search=()=>{
	let query=$("#search-input").val();
	
	if(query == ""){
		$(".search-result").hide();
	}else{
		console.log(query);
		
		
		//sending request to server
		
		let url=`http://localhost:8080/search/${query}`;
		fetch(url).then((response)=>{
			return response.json();
		}).then((data)=>{
			
			
			
	
			
			let text=`<div class='list-group'>`
			
			data.forEach((contacts)=>{
				
				text+=`<a href='/user/${contacts.cid}/contact' class='list-group-item list-group-action'>${contacts.name}  </a>`
				
			})
			
			text+=`</div>`;
				$(".search-result").html(text);
				$(".search-result").show();
				
		})
		
	
			$(".search-result").show();
	}
}
	