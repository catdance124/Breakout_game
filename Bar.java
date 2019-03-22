/*
** 自身が操作するバーのクラス
** 描画および移動ができる
*/

import java.awt.Color;
import java.awt.Graphics;

public class Bar {

  static int width,height;	// ウィンドウの大きさ

  int x;    // 左端の座標
  int y;    // バーの高さ
  int len;     // 長さ
  int dx;     // 移動量
  static int dx_default;    // 移動量の初期値
  int ax;     // 加速度
  boolean rFlag,lFlag;

  /*
   * コンストラクタ---------------------------------------------------------------------------------------------------------------
   */
  public Bar(){
    x = width/2;
    y = height-60;
    len = width/4;
    dx = width/90;
    dx_default = dx;
    ax = width/250;
    rFlag = false;
    lFlag = false;
  }

  /*
   * 描画---------------------------------------------------------------------------------------------------------------
   */
  public void draw(Graphics g){
    g.setColor(new Color(0, 0, 255));
    g.fillRect(x,y,len,15);
    g.setColor(new Color(255, 255, 255));
    g.drawRect(x,y,len,15);
  }

  /*
   * 左右に移動---------------------------------------------------------------------------------------------------------------
   */
  public void moveLeft(){
    dx += ax;
    if(0 < x) x = x-dx;
    else dx = 0;
    lFlag = true;
  }

  public void moveRight(){
    dx += ax;
    if(x+len < width) x = x+dx;
    else dx = 0;
    rFlag = true;
  }

  /*
   * 移動範囲の大きさの設定---------------------------------------------------------------------------------------------------------------
   */
  public static void setSize(int w,int h){
    width=w;
    height=h;
  }
}