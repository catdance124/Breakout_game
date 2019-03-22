/*
 * スコアクラス
*/

public class Score{

  int score;      // スコア

  /*
   * コンストラクタ----------------------------------------------------------------
   */
  public Score(){
    score = 0;
  }

  public void hitBlock(int barChain, int blockChain, double balldx){
    if(blockChain == 1) blockChain = 0;
    score += 10 + barChain + blockChain*2 + Math.abs(balldx)/3;
  }

  public void lostBall(){
    if(score >= 100)
      score -= 100;
    else
      score = 0;
  }

}