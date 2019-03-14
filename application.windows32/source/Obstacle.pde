class Obstacle {

  PVector position;
  float w,h;
  
  Obstacle(float x, float y, float w_, float h_) {
    position = new PVector(x,y);
    w = w_;
    h = h_;
  }

  void display() {
    stroke(0);
    fill(0);
    
    rect(position.x,position.y,w,h);
  }
}
