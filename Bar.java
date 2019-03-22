/*
** ���g�����삷��o�[�̃N���X
** �`�您��шړ����ł���
*/

import java.awt.Color;
import java.awt.Graphics;

public class Bar {

  static int width,height;	// �E�B���h�E�̑傫��

  int x;    // ���[�̍��W
  int y;    // �o�[�̍���
  int len;     // ����
  int dx;     // �ړ���
  static int dx_default;    // �ړ��ʂ̏����l
  int ax;     // �����x
  boolean rFlag,lFlag;

  /*
   * �R���X�g���N�^---------------------------------------------------------------------------------------------------------------
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
   * �`��---------------------------------------------------------------------------------------------------------------
   */
  public void draw(Graphics g){
    g.setColor(new Color(0, 0, 255));
    g.fillRect(x,y,len,15);
    g.setColor(new Color(255, 255, 255));
    g.drawRect(x,y,len,15);
  }

  /*
   * ���E�Ɉړ�---------------------------------------------------------------------------------------------------------------
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
   * �ړ��͈͂̑傫���̐ݒ�---------------------------------------------------------------------------------------------------------------
   */
  public static void setSize(int w,int h){
    width=w;
    height=h;
  }
}