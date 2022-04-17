var troops = [];
function unit(desig, dmg, spd, img){		
		this.designation=desig;
		this.damage=dmg;
		this.speed=spd;
		this.image=document.createElement('img');
		this.image.src=img;
		this.image.style="height:20px;Width:20px;left:160px;Position:absolute;";
		document.getElementById('thing').appendChild(this.image);
}
function animate(soldier){
		var pos = 160;
		var id = setInterval(frame, 30);
		function frame(){
			if(pos==1000){
				clearInterval(id);
			}
			else{
				pos+=soldier.speed;
				soldier.image.style.left=pos+'px';
			}
		}
}

var t=0;
var building = {
	designation: "Base",
	Health:100,
	buildSpeed:10,
	image:null,
	construct: function(){
		this.image=document.createElement('input');
		this.image.type="image"
		this.image.src='https://vignette3.wikia.nocookie.net/wargameeuropeanescalation/images/3/37/Fob.png/revision/latest?cb=20130812023756';
		this.image.id='buildingImage';
		this.image.style='Height:40px; Width:40px;Left:100px;Position:absolute;';
		document.getElementById('thing').appendChild(this.image);
		this.image.addEventListener("click",building.buildInfantry,false);
	},
	buildInfantry: function(){
		let i=new unit("Infantry"+t,10,2,"https://upload.wikimedia.org/wikipedia/commons/thumb/2/2d/NATO_Map_Symbol_-_Infantry.svg/2000px-NATO_Map_Symbol_-_Infantry.svg.png");
		troops[t]=i;
		t++;
		animate(i);		
	}
};

