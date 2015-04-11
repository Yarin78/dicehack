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

BLOCK_SIZE = 10
MAX_TERRAIN_HEIGHT = 150
MIN_TERRAIN_HEIGHT = 40

height = {}

var get_height = function(x) {
	if (height[x]) return height[x];
	height[x] = canvas.height - (Math.random() * (MAX_TERRAIN_HEIGHT - MIN_TERRAIN_HEIGHT) + MIN_TERRAIN_HEIGHT);
	return height[x]
}

// Draw everything
var render = function () {
	if (bgReady) {
		ctx.drawImage(bgImage, 0, 0);
	}

	ctx.beginPath();
	adj = Math.floor(scrollx / BLOCK_SIZE) * BLOCK_SIZE
	ctx.moveTo(-scrollx, canvas.height)

	for (x = adj; x <= canvas.width + adj + BLOCK_SIZE; x += BLOCK_SIZE) {
		ctx.lineTo(x - scrollx, get_height(x))

//		ctx.quadraticCurveTo(x - scrollx - BLOCK_SIZE / 2, (get_height(x) + get_height(x - BLOCK_SIZE)) * 0.8, x - scrollx, get_height(x))
	}

	ctx.lineTo(canvas.width + adj + BLOCK_SIZE - scrollx, canvas.height);
	ctx.closePath();

	var gradient=ctx.createLinearGradient(0,canvas.height,0,canvas.height-MAX_TERRAIN_HEIGHT);
    gradient.addColorStop("0","red");
    gradient.addColorStop("0.4","green");
    gradient.addColorStop("1.0","lightgreen");
    ctx.fillStyle=gradient;
	ctx.fill();

	ctx.strokeStyle="green";
	ctx.lineWidth=2;
	ctx.stroke();
};

var update = function(modifier) {
	scrollx += modifier;
	//console.log("x: " + scrollx);
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
