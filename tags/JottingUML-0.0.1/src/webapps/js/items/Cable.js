

function Cable(){this.initialize.apply(this, arguments)};
(function(_class,_super){
	Item.extend(_class, _super);
	_class.properties = Lang.copy(_super.properties, {
		lineType   : {type: "string", value:"normal"},
		lineRoute  : {type: "string", value:"N"},
		startType  : {type: "string", value:"none"},
		endType    : {type: "string", value:"none"},
		startText  : {type: "string", value:""},
		centerText : {type: "string", value:""},
		endText    : {type: "string", value:""},
		points     : {type: "Point[]", value:[]},
		startPoint : {type: "Point", value:null},
		endPoint   : {type: "Point", value:null},
	});

	$(function(){
		Actions.registerAction(_class.name, new CableAction(_class));
	});
	
	/**
	 * コンストラクタ。
	 */
	_class.prototype.initialize = function(coorBase) {
		Lang.initAttibutes(this, _class.properties);
		_super.prototype.initialize.apply(this, arguments);
		
		this.points = [];
		this.startPoint = new Coor();
		this.endPoint = new Coor();
		this.setStartPoint(new Coor(coorBase));
		this.setEndPoint(new Coor({origin:this.startPoint, x:20, y:20}));
	}
	
	_class.prototype.addPoint = function(xx,yy) {
		var idx = this.onPointIndex(xx,yy);
		if (idx >= 0) {
			var coor = new Coor({
				origin:this.startPoint, 
				origin2:this.endPoint,
			});
			coor.xy(xx,yy);
			
			this.points.splice(idx,0,coor);
			// clear handle.
			for (var i=0; i<this.points.length; i++) {
				this.points[i].handle = null;
			}
			EditBuffer.notice();
		}
	}
	_class.prototype.delPoint = function(no) {
		this.points.splice(no,1);
		for (var i=0; i<this.points.length; i++) {
			this.points[i].handle = null;
		}
		EditBuffer.notice();
	}
	
	_class.prototype.setStartPoint = function(item,ex,ey) {
		setPoint(this.startPoint, item,ex,ey);
	}
	_class.prototype.setEndPoint = function(item,ex,ey) {
		setPoint(this.endPoint, item,ex,ey);
	}
	_class.prototype.setPoint = function(item,ex,ey, no) {
		var coor = this.points[no];
		setPoint(coor, item,ex,ey);
	}
	_class.prototype.getPoint = function(no) {
		return this.points[no];
	}
	
	
	function setPoint(coor, item,ex,ey) {
		if (item) {
			coor.origin(item);
			coor.origin2(item.coorDiag);
			if (item.coorDiag) {
				var xy = CableUtil.edgePoint(item,ex,ey);
				if (xy) {
					coor.xy(xy.x, xy.y);
				} else {
					coor.setX(0.5);
					coor.setY(0.5);
				}
			} else {
				coor._x = 0;
				coor._y = 0;
			}
		} else {
			coor.setOrigin(null);
			coor.setOrigin2(null);
		}
		EditBuffer.notice();
	}
	_class.prototype.onPoint = function(tx,ty) {
		return this.onPointIndex(tx,ty)>=0;
	}
	_class.prototype.inRect = function(tx1,ty1,tx2,ty2) {
		var b = Util.getOutBounds(CableUtil.getLines(this));
		return tx1<=b.x1 && b.x2<=tx2 && ty1<=b.y1 && b.y2<=ty2;
	}
	_class.prototype.getOutBounds = function() {
		return Util.getOutBounds(CableUtil.getLines(this));
	}
	_class.prototype.onPointIndex = function(tx,ty) {
		var r = 3;
		var rx1 = tx-r;
		var ry1 = ty-r;
		var rx2 = tx+r;
		var ry2 = ty+r;
		var lines = CableUtil.getLines(this);
		
		for (var i=0; i<lines.length; i++) {
			var hits = Util.crossRectLineRaw(rx1,ry1,rx2,ry2, 
					lines[i].x1, lines[i].y1, lines[i].x2, lines[i].y2);
			if (hits.length>0) return i;
		}
		return -1;
	}

	_class.prototype.draw= function(dr) {
		var lines = CableUtil.getLines(this);
		with (this) {
			if (lineRoute == "S") {
				dr.drawLinesS(lines, lineType);
			} else {
				dr.drawLines(lines, lineType);
			}
	
			dr.drawArrow(startType, 
					lines[0].x2, lines[0].y2, lines[0].x1, lines[0].y1);
			
			var i = lines.length-1;
			dr.drawArrow(endType, 
					lines[i].x1, lines[i].y1, lines[i].x2, lines[i].y2);

			CableUtil.drawText(dr, lines[0], 0, this);
			CableUtil.drawText(dr, lines[Math.floor(lines.length/2)], 1, this);
			CableUtil.drawText(dr, lines[i], 2, this);
		}
		return this;
	}
	
	_class.prototype.getHandle = function(xx,yy) {
		with (this) {
			for (var i=0; i<points.length; i++) {
				var handle = points[i].handle;
				if (handle.isOnCoor(xx,yy)) return handle;
			}
		}
		return null;
	}
	
	function toEdge(coor1, coor2) {
		var points = Util.crossRectLine(coor1.origin(), coor1, coor2);
		if (points.length == 0) return {x:coor1.x(), y:coor1.y()};
		return points[0];
	}
	
	function makeHandle(coor, self, method, no) {
		if (coor.handle) return;
		var color = Color.HANDLE_VISIT;
		if (method == "setStartPoint") color = Color.HANDLE_START;
		if (method == "setEndPoint") color = Color.HANDLE_END;
		if (no != undefined) {
			coor.handle = new CableHandleRoute(coor, self, no);
		} else {
			coor.handle = new CableHandle(coor, self, method);
		}
		coor.handle.color = color;
	}
	_class.prototype.makeHandle = function(coor) {
		makeHandle(this.startPoint,this,"setStartPoint");
		makeHandle(this.endPoint,this,"setEndPoint");
		for (var i=0; i<this.points.length; i++) {
			makeHandle(this.points[i],this,"setPoint", i);
		}
	}	
	_class.prototype.drawHandle = function(dc) {
		this.makeHandle();
		this.startPoint.handle.draw(dc);
		this.endPoint.handle.draw(dc);
		for (var i=0; i<this.points.length; i++) {
			this.points[i].handle.draw(dc);
		}
	}
	_class.prototype.getHandle = function(xx,yy) {
		with (this) {
			if (startPoint.handle.onPoint(xx,yy)) return startPoint.handle;
			if (endPoint.handle.onPoint(xx,yy)) return endPoint.handle;
			for (var i=0; i<points.length; i++) {
				if (points[i].handle.onPoint(xx,yy)) return points[i].handle;
			}
		}
		return null;
	}
	_class.prototype.getMenu = function() {
		return "#cableMenu";
	}
	_class.prototype.getDialog = function() {
		return "#cableDialog";
	}
	
})(Cable,Item);


//EOF
