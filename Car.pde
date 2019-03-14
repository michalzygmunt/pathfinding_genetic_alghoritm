

class Vehicle{
  int x,y;
  float angle;
  float[] genotype;
  int isAlive; //0 - zyje (-1) - nie zyje  1 - dotarl do celu
  
  
  Vehicle(){
    angle = 0;
    x = 20;
    y = 350;
    isAlive = 0;
    genotype = new float[1000];
}

void setX(int x){
  this.x = x;
}

int getX(){
  return this.x;
}

void setY(int y){
  this.y = y;
}

int getY(){
  return this.y;
}

void setAngle(float angle){
  this.angle = angle;
}

float getAngle(){
  return this.angle;
}

void setState(int isAlive){
  this.isAlive = isAlive;
}

int getState(){
  return this.isAlive;
}

void resetVehicle(){
  x = 20;
  y = 350;
  isAlive = 0;
  angle = 0;
}

void random_genome(){
  for(int i = 0; i<1000;i++)
  {
    genotype[i] = random(-0.2,0.2);
  }
}

float[] get_genome()
{
  return genotype;
}

float get_genome_index(int index)
{
  return genotype[index];
}

void set_genome(float[] g)
{
  for(int i = 0; i < 1000; i++)
  genotype[i] = g[i];
}

void moving(int move){
  angle += genotype[move];
  x = x + int(2*cos(angle));
  y = y + int(2*sin(angle));
}

void krzyzowanie(Vehicle p1, Vehicle p2)
{
  
  float random;
  for(int i = 0; i < genotype.length; i++){
    random = random(0,1);
    if(random<0.5)
      genotype[i] = p1.get_genome_index(i);
    else
      genotype[i] = p2.get_genome_index(i);
      
      if(random<=mutationRate)
        genotype[i] = random(-0.2,0.2);
  }
  /*
  int crossover = int(random(genome.length));
  for(int i = 0; i < genome.length; i++){
     if(i>crossover) genome[i] = p1.get_genome_index(i);
     else
     genome[i] = p2.get_genome_index(i);
  }
  */
}


}
    
