package bargaining_game;

abstract class Bargain { 
	
	
	abstract public void game_tutorial(); //게임 설명	
	abstract public void game_start(); //게임 시작
	abstract public void game_calculate(int rank,int money); //점수 계산
	
	

}
