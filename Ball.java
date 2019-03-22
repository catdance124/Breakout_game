/***
 *** ボールクラス
 *** バー・ブロックに当たると跳ね返る
 *** 描画可能
 ***/
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

public class Ball {

  static int width,height;	// 移動範囲の大きさ

  int x,y;    // 中心座標
  int rad;    // 半径
  int barChain;     // バー連続数
  int blockChain;  // ブロック連鎖数
  double dx,dy;   // 移動量
  boolean moveFlag, exist;    // 移動開始フラグ，ボールが画面外かどうかのフラグ

  /*
   * コンストラクタ---------------------------------------------------------------------------------------------------
   */
  public Ball(Bar bar){
    rad=10;
    x=bar.x;
    y=bar.y-rad;
    dx=2*rand();
    dy=-2;
    barChain=0;
    blockChain=0;
    moveFlag=false;
    exist=true;
  }

  /*
   * 1か-1を発生させる関数----------------------------------------------------------------------------------------------
   */
  public int rand(){
    int n;
    do{
      n = new Random().nextInt(3) - 1;    // 0~2の乱数を作り-1で-1~1を作成,0をはじく
    }while(n == 0);
    return n;
  }

  /**
   * ボールの初期化---------------------------------------------------------------------------------------------------
   * */
  public void init(){
    if(exist == false){
      dx=2*rand();
      dy=-2;
      exist=true;
    }
  }

  /*
   * 描画---------------------------------------------------------------------------------------------------------------
   */
  public void draw(Graphics g){
    g.setColor(new Color(255, 0, 0));
    g.fillOval(x-rad,y-rad,2*rad,2*rad);
    g.setColor(new Color(255, 255, 255));
    g.drawOval(x-rad,y-rad,2*rad,2*rad);
  }

  /*
   * 移動---------------------------------------------------------------------------------------------------------------
   * 壁・バー・ブロックに当たると跳ね返る
   */
  public void move(Bar bar, Vector<Block> blocks, Score score){
    if(!moveFlag && exist){         // 移動フラグがなく，存在しているときバーの上に球を表示
      x=bar.x+bar.len/2;
      y=bar.y-rad-1;
    }

    if(moveFlag && exist){        // 移動させる
      x+=dx;
      y+=dy;

      // 壁との当たり判定               画面下は抜ける
      if(x-rad<=0 || x+rad>=width) dx=-dx;
      if(y-rad<=0) dy=-dy;


      // バーとの当たり判定
      if(y > height+rad){               // ボールが画面の下に行ったとき球の存在フラグをfalse
        exist=false;
        moveFlag=false;
        score.lostBall();
        barChain=0;                           // 連鎖数を0にする
        blockChain=0;
      }
      else if(y > bar.y){                 // ボールがバーの下に行ったとき以下の判定を行わない
      }
      else if( (bar.x < x) && (x < bar.x+bar.len) && (y+rad >= bar.y) ){   // ボールがバーの上面に当たったとき
        dy = -dy;                               // dyを反転
        barChain++;                              // 連鎖数を+1
        blockChain=0;                         // ブロック連鎖数を0
        if(bar.dx > bar.dx_default){      // バーに速度があるとき
          if(bar.lFlag){                        // バーが左に移動しているとき
            dx -= bar.dx/9;
          }
          if(bar.rFlag){          // バーが右に移動しているとき
            dx += bar.dx/9;
          }
        }
      }
      else if((x-bar.x)*(x-bar.x) + (y-bar.y)*(y-bar.y) <= rad*rad){         // ボールがバーの左上角に当たったとき
        if(dx>0)  dx = -dx;       // 左から当たったときのみx反転
        dy = -dy;
        barChain++;                              // 連鎖数を+1
        blockChain=0;                         // ブロック連鎖数を0
      }
      else if((x-bar.x-bar.len)*(x-bar.x-bar.len) + (y-bar.y)*(y-bar.y) <= rad*rad){     // 右上角
        if(dx<0)  dx = -dx;       // 右から当たったときのみx反転
        dy = -dy;
        barChain++;                              // 連鎖数を+1
        blockChain=0;                         // ブロック連鎖数を0
      }


      // ブロックとの当たり判定
      for(Block block:blocks)
        if(block.exist){
          if( (block.x <= x) && (x <= block.x+block.x_len) &&               // xがブロックの範囲内
              (y+rad >= block.y) && (y-rad <= block.y+block.y_len)){      // yが上下に接する->ブロックに上下から衝突
            dy = -dy;
            block.exist = false;
            blockChain++;                     // ブロック連鎖数を+1
            score.hitBlock(barChain, blockChain, dx);
          }
          if( (block.x <= x+rad) && (x-rad <= block.x+block.x_len) &&         // yがブロックの範囲内
                  (y >= block.y) && (y <= block.y+block.y_len)){          // xが左右に接する->ブロックに左右から衝突
            dx = -dx;
            block.exist = false;
            blockChain++;                     // ブロック連鎖数を+1
            score.hitBlock(barChain, blockChain, dx);
          }
        }
    }

  }

  /*
   * 移動範囲の大きさの設定---------------------------------------------------------------------------------------------
   */
  public static void setSize(int w,int h){
    width=w;
    height=h;
  }

}