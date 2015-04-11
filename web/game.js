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

var turretReady = false
var turretImage = new Image();
turretImage.onload = function () {
    turretReady = true;
};
turretImage.src = "images/turret.png";

var explosionReady = false
var explosionImage = new Image();
explosionImage.onload = function () {
    turretReady = true;
};
explosionImage.src = "images/explosion.gif";

BLOCK_SIZE = 100
MAX_TERRAIN_HEIGHT = 150
MIN_TERRAIN_HEIGHT = 40

TURRET_HEIGHT = 20
TURRET_WIDTH = 20
TURRET_BASELINE = 10
TURRET_PROBABILITY = 0.01
MIN_TURRET_DISTANCE = 150
MAX_TURRETS = 20

AIM_X = 500
AIM_Y = 500

height = {}
turrets = [500]
var missiles = [];


var get_height = function(x) {
	if (height[x]) return height[x];
	height[x] = canvas.height - (Math.random() * (MAX_TERRAIN_HEIGHT - MIN_TERRAIN_HEIGHT) + MIN_TERRAIN_HEIGHT);
	return height[x]
}

var can_shoot = function(turret) {
	for(i = 0; i < missiles.length; i++) {
		if (missiles[i].turret == turret && missiles[i].recharge > 0)
			return false;
	}
	return true;
}

var update_missiles = function() {
	for(i = 0; i < missiles.length; i++) {
		missiles[i].recharge -= 1;
		missiles[i].x += missiles[i].dx;
		missiles[i].y += missiles[i].dy;
	}
}

var update_turrets = function() {
	// Any turret that's out of picture?
	while (turrets.length > 0 && turrets[0] < scrollx) {
		turrets.splice(0, 1);
	}

	// Randomize turret shots
	for(i = 0; i < turrets.length; i++) {
		if (can_shoot(turrets[i])) {
			if (Math.random() < 0.05) {
//				console.log("shoot " + turrets[i]);
				missiles.splice(missiles.length, 0, {
					turret: turrets[i],
					recharge: 50,
					x: turrets[i],
					y: get_ground_height(turrets[i])
				});
			}
		}
	}

	if (turrets.length > 0 && turrets[turrets.length-1] + MIN_TURRET_DISTANCE > scrollx + canvas.width) {
		return;
	}

	if (turrets.length < MAX_TURRETS && Math.random() < TURRET_PROBABILITY) {
		turrets.splice(turrets.length, 0, scrollx + canvas.width);
	}

}

// Game objects
var spaceship = {
    speed: 200, // vertical movement speed
    x: 20,
    y: 0
}
SPACESHIP_HEIGHT = 150;

var spaceshipReady = false;
var spaceshipImage = new Image();
spaceshipImage.onload = function () {
    spaceshipReady = true;
};
spaceshipImage.src = "images/spaceship.gif";

var laser = {
    speed: 500,
    x: -1000,
    y: 0,
    rechargeCountdown: 0,
    rechargeCost: 1.2
}

var explosion = {
    object: null,
    countdown: 0
}

// Handle keyboard controls
var keysDown = {};

addEventListener("keydown", function (e) {
    keysDown[e.keyCode] = true;
}, false);

addEventListener("keyup", function (e) {
    delete keysDown[e.keyCode];
}, false);

var draw_turret = function(x, y) {
	ctx.beginPath();
	ctx.moveTo(x - TURRET_WIDTH / 2, y);
	ctx.lineTo(x - TURRET_WIDTH / 2 + 5, y - TURRET_HEIGHT);
	ctx.lineTo(x + TURRET_WIDTH / 2 - 5, y - TURRET_HEIGHT);
	ctx.lineTo(x + TURRET_WIDTH / 2, y);
	ctx.closePath();

	var gradient = ctx.createLinearGradient(0,y,0,y - TURRET_HEIGHT);
	gradient.addColorStop("0","blue");
	gradient.addColorStop("1.0","magenta");
	ctx.fillStyle=gradient;
	ctx.fill();

	ctx.beginPath();
	ctx.moveTo(x, y - TURRET_HEIGHT);
	ctx.lineTo(x, y - TURRET_HEIGHT - 20);
	ctx.closePath();
	ctx.strokeStyle = "red";
	ctx.stroke();

}

var get_ground_height = function(x) {
	x1 = Math.floor(x / BLOCK_SIZE) * BLOCK_SIZE;
	x2 = Math.floor(x / BLOCK_SIZE) * BLOCK_SIZE + BLOCK_SIZE;
	y = (get_height(x2) - get_height(x1)) * (x - x1) / BLOCK_SIZE + get_height(x1);
	return y;
}

var explode = function (obj) {
    explosion.countdown = 0.5;
    explosion.object = obj;
}

// Draw everything
var render = function () {
	if (bgReady) {
		ctx.drawImage(bgImage, 0, 0);
	}

	ctx.beginPath();
	adj = Math.floor(scrollx / BLOCK_SIZE) * BLOCK_SIZE
	ctx.moveTo(-scrollx, canvas.height)

	for (x = adj; x <= canvas.width + adj + BLOCK_SIZE*2; x += BLOCK_SIZE) {
		ctx.lineTo(x - scrollx, get_height(x))
	}

	ctx.lineTo(canvas.width + adj + BLOCK_SIZE*2 - scrollx, canvas.height);
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

    // Space ship
	if (spaceshipReady) {
	    ctx.drawImage(spaceshipImage, spaceship.x, spaceship.y);
	}

    // Turrets
	for(i = 0; i < turrets.length; i++) {
		y = get_ground_height(turrets[i])
	    draw_turret(turrets[i] - scrollx, y + TURRET_BASELINE);
	}



    // Laser
	ctx.beginPath();
	ctx.moveTo(laser.x, laser.y);
	ctx.lineTo(laser.x + 50, laser.y + 50);
	ctx.lineTo(laser.x + 45, laser.y + 50);
	ctx.lineTo(laser.x - 5, laser.y);
	ctx.closePath();
	ctx.strokeStyle = "green";
	ctx.lineWidth = 2;
	ctx.stroke();

    // Explosion
	if (explosion.object != null) {
	    ctx.drawImage(explosionImage, explosion.object.x, explosion.object.y);
	}
};

var update = function(modifier) {
	scrollx += 50 * modifier;
    //console.log("x: " + scrollx);
	spaceship.y += ((40 in keysDown) - (38 in keysDown)) * spaceship.speed * modifier;

    update_turrets();
    update_missiles();

	if (spaceship.y < 0)
	    spaceship.y = 0;
	else if (spaceship.y > canvas.height - 150)
	    spaceship.y = canvas.height - 150;

	if (spaceship.y > get_ground_height(spaceship.x + scrollx) - 150) {
	    explode(spaceship);
	}

	if (laser.rechargeCountdown <= 0 && (32 in keysDown)) {
	    laser.x = spaceship.x + 50;
	    laser.y = spaceship.y + 87;
	    laser.rechargeCountdown = laser.rechargeCost;
	}
	if (laser.rechargeCountdown > 0) {
	    laser.rechargeCountdown -= modifier;
	    laser.x += laser.speed * modifier;
	    laser.y += laser.speed * modifier;
	}

	if (explosion.countdown > 0) {
	    explosion.countdown -= modifier;
	} else {
	    if (explosion.object == spaceship) {
            alert("Game Over!")
	    }
	    explosion.object = null;
	}
}

// The main game loop
var main = function () {
	var now = Date.now();
	var delta = now - then;

	update(delta / 1000);
	render();

	then = now;

	// Request to do this again ASAP
	requestAnimationFrame(main);
};

// Let's play this game!
var then = Date.now();
scrollx = 0
main();
