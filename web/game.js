// Create the canvas
var canvas = document.createElement("canvas");
var ctx = canvas.getContext("2d");
canvas.width = 1300;
canvas.height = 600;
document.body.appendChild(canvas);


var bgReady = false;
var bgImage = new Image();
bgImage.onload = function () {
	bgReady = true;
};
bgImage.src = "images/outer-space.jpg";

var pseudo_height = function(x) {
	x = Math.round(x / 50)

}

var ground_level = function(x) {

}

// Draw everything
var render = function () {
	if (bgReady) {
		ctx.drawImage(bgImage, 0, 0);
	}

	ctx.beginPath();

	ctx.moveTo(200 + scrollx, 30);
	ctx.lineTo(300 + scrollx, 100);
	ctx.strokeStyle="red";
	ctx.lineWidth=5;
	ctx.stroke();


	// Score
	ctx.fillStyle = "rgb(250, 250, 250)";
	ctx.font = "24px Helvetica";
	ctx.textAlign = "left";
	ctx.textBaseline = "top";
	//ctx.fillText("Monsterrs caught: " + monstersCaught, 32, 32);
};

var update = function(modifier) {
	scrollx += modifier;
	console.log("x: " + scrollx);
}

// The main game loop
var main = function () {
	var now = Date.now();
	var delta = now - then;

	update(delta / 1000 * 50);
	render();

	then = now;

	// Request to do this again ASAP
	requestAnimationFrame(main);
};

// Let's play this game!
var then = Date.now();
scrollx = 0
main();
