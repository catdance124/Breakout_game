/***
 *** ブロッククラス
 *** 描画可能
 ***/
import java.awt.Color;
import java.awt.Graphics;

public class Block {

  int x,y;        // 座標
  static int x_len ;      // ブロックの
  static int y_len;
  Color col;
  boolean exist;

  /*
   * コンストラクタ----------------------------------------------------------------------
   * 引数あり，指定された座標にブロックを既定の大きさ・色
   */
  public Block(int x, int y, Color col){
    this.x = x;
    this.y = y;
    this.col = col;
    exist = true;
  }

  /*
   * 描画-----------------------------------------------------------------------------------
   */
  public void draw(Graphics g){
    g.setColor(col);
    g.fillRect(x,y,x_len,y_len);
    g.setColor(new Color(255, 255, 255));
    g.drawRect(x,y,x_len,y_len);
  }

  /*
   * ブロックの大きさの設定-------------------------------------------------------------
   */
  public static void setSize(int w,int h){
    x_len=w/6;
    y_len=h/18;
  }
}