/***
 *** �u���b�N�N���X
 *** �`��\
 ***/
import java.awt.Color;
import java.awt.Graphics;

public class Block {

  int x,y;        // ���W
  static int x_len ;      // �u���b�N��
  static int y_len;
  Color col;
  boolean exist;

  /*
   * �R���X�g���N�^----------------------------------------------------------------------
   * ��������C�w�肳�ꂽ���W�Ƀu���b�N������̑傫���E�F
   */
  public Block(int x, int y, Color col){
    this.x = x;
    this.y = y;
    this.col = col;
    exist = true;
  }

  /*
   * �`��-----------------------------------------------------------------------------------
   */
  public void draw(Graphics g){
    g.setColor(col);
    g.fillRect(x,y,x_len,y_len);
    g.setColor(new Color(255, 255, 255));
    g.drawRect(x,y,x_len,y_len);
  }

  /*
   * �u���b�N�̑傫���̐ݒ�-------------------------------------------------------------
   */
  public static void setSize(int w,int h){
    x_len=w/6;
    y_len=h/18;
  }
}