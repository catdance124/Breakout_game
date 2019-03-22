/***
 *** �{�[���N���X
 *** �o�[�E�u���b�N�ɓ�����ƒ��˕Ԃ�
 *** �`��\
 ***/
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

public class Ball {

  static int width,height;	// �ړ��͈͂̑傫��

  int x,y;    // ���S���W
  int rad;    // ���a
  int barChain;     // �o�[�A����
  int blockChain;  // �u���b�N�A����
  double dx,dy;   // �ړ���
  boolean moveFlag, exist;    // �ړ��J�n�t���O�C�{�[������ʊO���ǂ����̃t���O

  /*
   * �R���X�g���N�^---------------------------------------------------------------------------------------------------
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
   * 1��-1�𔭐�������֐�----------------------------------------------------------------------------------------------
   */
  public int rand(){
    int n;
    do{
      n = new Random().nextInt(3) - 1;    // 0~2�̗��������-1��-1~1���쐬,0���͂���
    }while(n == 0);
    return n;
  }

  /**
   * �{�[���̏�����---------------------------------------------------------------------------------------------------
   * */
  public void init(){
    if(exist == false){
      dx=2*rand();
      dy=-2;
      exist=true;
    }
  }

  /*
   * �`��---------------------------------------------------------------------------------------------------------------
   */
  public void draw(Graphics g){
    g.setColor(new Color(255, 0, 0));
    g.fillOval(x-rad,y-rad,2*rad,2*rad);
    g.setColor(new Color(255, 255, 255));
    g.drawOval(x-rad,y-rad,2*rad,2*rad);
  }

  /*
   * �ړ�---------------------------------------------------------------------------------------------------------------
   * �ǁE�o�[�E�u���b�N�ɓ�����ƒ��˕Ԃ�
   */
  public void move(Bar bar, Vector<Block> blocks, Score score){
    if(!moveFlag && exist){         // �ړ��t���O���Ȃ��C���݂��Ă���Ƃ��o�[�̏�ɋ���\��
      x=bar.x+bar.len/2;
      y=bar.y-rad-1;
    }

    if(moveFlag && exist){        // �ړ�������
      x+=dx;
      y+=dy;

      // �ǂƂ̓����蔻��               ��ʉ��͔�����
      if(x-rad<=0 || x+rad>=width) dx=-dx;
      if(y-rad<=0) dy=-dy;


      // �o�[�Ƃ̓����蔻��
      if(y > height+rad){               // �{�[������ʂ̉��ɍs�����Ƃ����̑��݃t���O��false
        exist=false;
        moveFlag=false;
        score.lostBall();
        barChain=0;                           // �A������0�ɂ���
        blockChain=0;
      }
      else if(y > bar.y){                 // �{�[�����o�[�̉��ɍs�����Ƃ��ȉ��̔�����s��Ȃ�
      }
      else if( (bar.x < x) && (x < bar.x+bar.len) && (y+rad >= bar.y) ){   // �{�[�����o�[�̏�ʂɓ��������Ƃ�
        dy = -dy;                               // dy�𔽓]
        barChain++;                              // �A������+1
        blockChain=0;                         // �u���b�N�A������0
        if(bar.dx > bar.dx_default){      // �o�[�ɑ��x������Ƃ�
          if(bar.lFlag){                        // �o�[�����Ɉړ����Ă���Ƃ�
            dx -= bar.dx/9;
          }
          if(bar.rFlag){          // �o�[���E�Ɉړ����Ă���Ƃ�
            dx += bar.dx/9;
          }
        }
      }
      else if((x-bar.x)*(x-bar.x) + (y-bar.y)*(y-bar.y) <= rad*rad){         // �{�[�����o�[�̍���p�ɓ��������Ƃ�
        if(dx>0)  dx = -dx;       // �����瓖�������Ƃ��̂�x���]
        dy = -dy;
        barChain++;                              // �A������+1
        blockChain=0;                         // �u���b�N�A������0
      }
      else if((x-bar.x-bar.len)*(x-bar.x-bar.len) + (y-bar.y)*(y-bar.y) <= rad*rad){     // �E��p
        if(dx<0)  dx = -dx;       // �E���瓖�������Ƃ��̂�x���]
        dy = -dy;
        barChain++;                              // �A������+1
        blockChain=0;                         // �u���b�N�A������0
      }


      // �u���b�N�Ƃ̓����蔻��
      for(Block block:blocks)
        if(block.exist){
          if( (block.x <= x) && (x <= block.x+block.x_len) &&               // x���u���b�N�͈͓̔�
              (y+rad >= block.y) && (y-rad <= block.y+block.y_len)){      // y���㉺�ɐڂ���->�u���b�N�ɏ㉺����Փ�
            dy = -dy;
            block.exist = false;
            blockChain++;                     // �u���b�N�A������+1
            score.hitBlock(barChain, blockChain, dx);
          }
          if( (block.x <= x+rad) && (x-rad <= block.x+block.x_len) &&         // y���u���b�N�͈͓̔�
                  (y >= block.y) && (y <= block.y+block.y_len)){          // x�����E�ɐڂ���->�u���b�N�ɍ��E����Փ�
            dx = -dx;
            block.exist = false;
            blockChain++;                     // �u���b�N�A������+1
            score.hitBlock(barChain, blockChain, dx);
          }
        }
    }

  }

  /*
   * �ړ��͈͂̑傫���̐ݒ�---------------------------------------------------------------------------------------------
   */
  public static void setSize(int w,int h){
    width=w;
    height=h;
  }

}