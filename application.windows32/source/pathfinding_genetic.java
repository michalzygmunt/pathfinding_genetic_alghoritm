import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class pathfinding_genetic extends PApplet {




int population = 60;
float mutationRate = 0.01f;
boolean flaga = true;
float[] pop_fit_after_sort = new float[population];
int[] pop_index_after_sort = new int[population];
int[] goal_turn = new int[population];
float[] goal_closest = new float[population];
float[] top2_score = new float[2];
float[] top1 = new float[1000];
float[] top2 = new float[1000];

float top_fitness;
int turn = 0;
int gen = 1;
int deads = 0;
int div = 0;
int bestDriver = 999;
float[] avg_fitness = new float[population];
Vehicle[] generation = new Vehicle[population];
boolean[] hitObstacle = new boolean[population];
float avg = 0;
float[][] slad = new float[population][1000];
float[] tracex = new float[1000];
float[] tracey = new float[1000];
float goal_reached;
float last_goal_reached;
int m1;
int m2;
ArrayList<Obstacle> obstacles;
float[] sort_index = new float[population];
int[] tab_index = new int[population];
Random generator = new Random();

public void setup() {

  
  frameRate(150);
  
  ellipse(20, 350, 10, 10);
  top2_score[0] = 0;
  top2_score[1] = 0;
  obstacles = new ArrayList<Obstacle>();
  
  

  for (int z = 0; z < population; z++) {
    hitObstacle[z] = false;
    generation[z] = new Vehicle();
    generation[z].random_genome();
    goal_turn[z] = 1000;
    goal_closest[z] = 1000;
    avg_fitness[z] = 0;
    goal_reached = 0;
    last_goal_reached = 0;
  }
}

public void draw() {

 
  for (Obstacle obs : obstacles) {
    obs.display();
  }
 
  
  if (turn<1000)
  {
    
    background(0xffF0F8FF);
    
   if(gen>1 && turn == 1){
    println("Average Fitness: " +avg);
    
  } 
  
    
  if(turn == 1){
    println("GENERATION#" + gen);
  }
  
  

   
    //przeszkody
       
    rect(200, 450, 3, 150);
    rect(200, 150, 3, 150);
     
    rect(500, 450, 3, 150);
    rect(500, 150, 3, 150);
  rect(360,260, 3 , 260);
  
  
   // rect (600, 430, 3, 80);
  //  rect (700,220, 3, 80);
  //  rect(650,333, 3 ,80);
  //  rect( 550, 280, 3 ,80);
  //  rect(300, 333, 120, 3);
    
  //  rect(300, 433, 120, 3);
   
     
      
    rect(0, 150, 800, 1);
    rect(0, height-2, 800, 1);
    rect(0, 150, 1, 450);
    rect(width-2, 150, 1, 450);



    //CEL
    noStroke();
    fill(0xff00FF00);
    star(760, 350, 18, 8,5);
    stroke(0);

    //UPDATE POZYCJI
    for (int i = 0; i < population; i++) {
      // jezeli organizm zyje
      if (generation[i].getState() == 0) {
        //aktualizujemy pozycje
        generation[i].moving(turn);
        
        

        float dist;
        //dystans osobnika od celu
        dist = dist(generation[i].getX(), generation[i].getY(), 760, 350);
        //jezeli dystans jest mniejszy od najlepszego, to zamieniamy
        if (dist<goal_closest[i])  // closest distance ever for fitness calculation
          goal_closest[i] = dist;

        //PRZESZKODY
        if (get(generation[i].getX(), generation[i].getY())==0xff000000)
        {
          generation[i].setState(-1);
          hitObstacle[i] = true;
          deads++;
          goal_turn[i]=turn;
        } else
        {
          //JEZELI DOTRZE DO CELU
          if (get(generation[i].getX(), generation[i].getY()) ==0xff00FF00)
          {
            goal_reached++;
            generation[i].setState(1);
            goal_turn[i] = turn;            //liczba kroków poszczegolnego osobnika, jezeli osiagnie cel
            deads++;
            println("dotarl do celu w czasie: "+turn);
     //       println("goal_turn = "+ goal_turn[i]);
            if(turn<bestDriver)
              bestDriver = turn;
          }
        }
      }
    }

    //RUSUJEMY SAMOCHODY

    for (int i = 0; i <population; i++) {
      pushMatrix();
      tracex[i] = generation[i].getX();
      tracey[i] = generation[i].getY();
      
      
      
     
      float final_dist = dist(generation[i].getX(), generation[i].getY(), 760, 350);
      if(final_dist<200){
        fill(0xffF52233);}
        else
        fill(0xff008080);
        //  rect(50,50,25,8); //glowna czesc
        /*  
          //gora samochodu
          fill(#00FFFF);
          rect(53,45,10,5);         
          //rura
          fill(#00FF00);
          rect(48,50,2,2);
          //kola
          fill(#FF0000);
          ellipse(55,56,6,6);
          ellipse(69,56,6,6);
          
  */  
    // triangle(-4, -8, +4, -8, 0, 8);
  
      
     translate(generation[i].getX(), generation[i].getY());
      rotate(generation[i].getAngle()-PI/2);
      fill(0xffFF8C00);
      rect(-8,-19,8,19);
      
     // translate(generation[i].getX()+3, generation[i].getY()-5);
    //  rotate(generation[i].getAngle()-PI/2);
      fill(0xffADD8E6);   
      rect(0,-19,5,9);    //gora
      fill(0xffFF0000);
      ellipse(-6,-17,4,4); //kola
      ellipse(-6,-7,4,4);
      //rura
          fill(0xff00FF00);
          rect(-1,-23,2,2);
     
      
      popMatrix();
      
    }
    
    
    fill(0xffF0FFFF);
    rect(0,0,800,150);     
   
    
    textSize(20);
    fill(0xff000080);
    text("Generation no. "+gen, 10,30);
    text("Vehicles left: " + (population-deads), 10, 55);
    text("Best Driver: " + bestDriver, 10,80);
    text("Cycles Left: "+(1000-turn),10,105);
    text("Total Population: " + population, 200, 30);
    text("Mutation Rate: " + mutationRate, 200,55);
    text("Top fitness " + top_fitness, 200, 80);
    text("Avg fitness: " + avg,200,105);
    text("Goal reached: " + nf(goal_reached),430,30);
    text("Last Goal reached: " + nf(last_goal_reached),430,55);

    //check if last turn
    if (turn==999 || deads == population)
    {
      for (int i = 0; i<population; i++)
      {
        float score;
        float fitness;
        float final_dist = dist(generation[i].getX(), generation[i].getY(), 760, 350);
        //FITNESS FUNKCJA
        if (generation[i].getState() == 1)
        {
          score = (1000-(final_dist))* 1.4f + (1000 - goal_turn[i]) * 1.2f;
          sort_index[i] = score;
       //   float bonus = score *0.5;
       //   score += bonus;
       //   bonus = (1000 - goal_turn[i]) * 0.2;
       //   score += bonus;
       //   score = pow(score,4);
        }  
        else
         { 
           
          
            
          score = (1000-(final_dist)) + (1000-goal_closest[i]) * 0.3f;
          if(goal_turn[i] < 500){
            score*= 0.9f;
          }
          
          if(gen>1 && score > avg){
            score += 20;
          }
          sort_index[i] = score;
          
        //  float bonus = (1000 - final_dist) * 0.1;
        //  score += bonus;
          
         // if(final_dist<500){
        //  score *= 0.9;
        //  }
        //  if(hitObstacle[i] == false && final_dist > 500){
       //     score *= 0.5;
      //    }
         // bonus = ((0.1)*goal_closest);
       //   score = pow(score,4);
      //    score = map(fitness,0,100,0,1);
         }
        avg_fitness[i] = score;



//selekcja
 //   float prob_kill_step = (100/population)/1;
  //   if(flaga == true){
 //   tab_index = sort_population();
  //  println(tab_index);
 //    flaga = false;
 //    }
     
  //  for( int p = population -1; p>=0; p--)
 //   {
 //     float rand = random(0,100);
 //     if(rand < prob_kill_step*k)
//      {
        
      

        if (score > top2_score[0]) {
          top2_score[1] = top2_score[0];
          top2 = copy_genome(top1);
          top2_score[0] = score;
          top1 = copy_genome(generation[i].get_genome());
        } else
          if (score>top2_score[1] && i!=0)
          {
            top2_score[1] = score;
            top2 = copy_genome(generation[i].get_genome());
          }
 
      
}
      top_fitness = top2_score[0];
   /*  
      top3 = copy_genome(generation[tab_index[population-3]].get_genome());
      top4 = copy_genome(generation[tab_index[population-4]].get_genome());
      top5 = copy_genome(generation[tab_index[population-5]].get_genome());
      top6 = copy_genome(generation[tab_index[population-6]].get_genome());
      top7 = copy_genome(generation[tab_index[population-7]].get_genome());
      top8 = copy_genome(generation[tab_index[population-8]].get_genome());
      top9 = copy_genome(generation[tab_index[population-9]].get_genome());
      top10 = copy_genome(generation[tab_index[population-10]].get_genome());
      top_fitness = top2_score[0];
      generation[0].set_genome(top1);
      generation[1].set_genome(top2);
       generation[2].set_genome(top3);
      generation[3].set_genome(top4);
       generation[4].set_genome(top5);
      generation[5].set_genome(top6);
       generation[6].set_genome(top7);
      generation[7].set_genome(top8);
       generation[8].set_genome(top9);
      generation[9].set_genome(top10);
*/
      generation[0].set_genome(top1);
      generation[1].set_genome(top2);
      
      for (int n = 2; n<population; n++) {
        
        
        generation[n].krzyzowanie(generation[0], generation[1]);
      }

      for (int i = 0; i < population; i++)
      {
        
        generation[i].resetVehicle();
        goal_turn[i] = 1000;
        goal_closest[i] = 1000;
      }
    if(gen>1){
    last_goal_reached = goal_reached;
  }
  goal_reached = 0;
  
    turn = 0;
    deads = 0;
    gen++;
    flaga = true;
    
    //obliczanie sredniego fitness
    if(gen>1){
    avg = 0;
   
    for(int i = 0; i < population; i++){
      
      avg+= (avg_fitness[i])/population;
      
    }
    }
  }
  turn++;
  
}

}


public int[] sort_population(){
        int temp = 0;
        Map<Float, Integer> myMap = new HashMap<Float, Integer>();
        for(int i = 0; i< population; i++){
          myMap.put(sort_index[i], i);
        }
        Map<Float,Integer> treeMap = new TreeMap<Float,Integer>(myMap);
        
        for (Float key : treeMap.keySet()) {
    int value = treeMap.get(key);
  //  println(value);
    println(key);
    pop_fit_after_sort[temp] = key;
    pop_index_after_sort[temp] = value;
    println(pop_index_after_sort[temp]);
    temp++;
}
      top_fitness = pop_fit_after_sort[population-1];
        
   for(int i = 0; i > population; i++){
       println("best fitness pop: " + pop_fit_after_sort[i]);
       println("and it index: " + pop_index_after_sort[i]);
       
          
   }
  
  return pop_index_after_sort;
  
}




public float[] copy_genome(float[] old)
{
  float[] new_ = new float[1000];
  for (int i=0; i<1000; i++)
    new_[i]=old[i];
  return new_;
}  

public void star(float x, float y, float radius1, float radius2, int npoints) {
  float angle = TWO_PI / npoints;
  float halfAngle = angle/2.0f;
  beginShape();
  for (float a = 0; a < TWO_PI; a += angle) {
    float sx = x + cos(a) * radius2;
    float sy = y + sin(a) * radius2;
    vertex(sx, sy);
    sx = x + cos(a+halfAngle) * radius1;
    sy = y + sin(a+halfAngle) * radius1;
    vertex(sx, sy);
  }
  endShape(CLOSE);
}
//piszemy prace w word
//napisz metode selekcji, 


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

public void setX(int x){
  this.x = x;
}

public int getX(){
  return this.x;
}

public void setY(int y){
  this.y = y;
}

public int getY(){
  return this.y;
}

public void setAngle(float angle){
  this.angle = angle;
}

public float getAngle(){
  return this.angle;
}

public void setState(int isAlive){
  this.isAlive = isAlive;
}

public int getState(){
  return this.isAlive;
}

public void resetVehicle(){
  x = 20;
  y = 350;
  isAlive = 0;
  angle = 0;
}

public void random_genome(){
  for(int i = 0; i<1000;i++)
  {
    genotype[i] = random(-0.2f,0.2f);
  }
}

public float[] get_genome()
{
  return genotype;
}

public float get_genome_index(int index)
{
  return genotype[index];
}

public void set_genome(float[] g)
{
  for(int i = 0; i < 1000; i++)
  genotype[i] = g[i];
}

public void moving(int move){
  angle += genotype[move];
  x = x + PApplet.parseInt(2*cos(angle));
  y = y + PApplet.parseInt(2*sin(angle));
}

public void krzyzowanie(Vehicle p1, Vehicle p2)
{
  
  float random;
  for(int i = 0; i < genotype.length; i++){
    random = random(0,1);
    if(random<0.5f)
      genotype[i] = p1.get_genome_index(i);
    else
      genotype[i] = p2.get_genome_index(i);
      
      if(random<=mutationRate)
        genotype[i] = random(-0.2f,0.2f);
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
    
class Obstacle {

  PVector position;
  float w,h;
  
  Obstacle(float x, float y, float w_, float h_) {
    position = new PVector(x,y);
    w = w_;
    h = h_;
  }

  public void display() {
    stroke(0);
    fill(0);
    
    rect(position.x,position.y,w,h);
  }
}
  public void settings() {  size(800, 600);  smooth(4); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "pathfinding_genetic" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
