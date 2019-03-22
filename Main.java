/***
 *** �u���b�N�������C���\�[�X
 ***/

import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class Main extends Applet implements Runnable,KeyListener {

  Thread runner;    // ���s�p�X���b�h
  int width,height;   // �A�v���b�g�̑傫��
  Image off;      // ���`��p�I�t�X�N���[���C���[�W
  Image backGroundImage;  // �w�i
  Image questclear, questclear_t;
  BufferedImage bufquestclear;
  Graphics offg;      // ���̃O���t�B�b�N�X
  Vector <Block> blocks;      // �ϒ��z��
  Bar bar;      // �o�[�̃C���X�^���X
  Ball ball;      // �{�[���̃C���X�^���X
  Score score;      // �X�R�A�̃C���X�^���X
  boolean complete;   //�N���A�t���O
  int count;            // �������u���b�N�J�E���g�p

  /*
   * ������--------------------------------------------------------------------------------------------------------------------
   */
  public void init(){
    // �w�i�ݒ�
    Toolkit toolkit=Toolkit.getDefaultToolkit();
    backGroundImage=toolkit.getImage(
        getClass().getResource("images/matrix.gif") );
    questclear=toolkit.getImage(
        getClass().getResource("images/questclear_red.jpg") );
    MediaTracker tracker = new MediaTracker(new Component(){});
        tracker.addImage(questclear, 0);
        try {
          tracker.waitForID(0);
        } catch (InterruptedException e) {}
    bufquestclear = createBufferedImage(questclear);
    questclear_t = getTransparentImage( bufquestclear );

    // �u���b�N�z��
    blocks=new Vector<Block>();

    // �I�t�X�N���[���C���[�W�̎擾
    width=getSize().width;
    height=getSize().height;
    off=createImage(width,height);
    offg=off.getGraphics();

    // �T�C�Y�ݒ�
    bar.setSize(width,height);
    ball.setSize(width,height);
    Block.setSize(width,height);

    bar = new Bar();    // �o�[
    ball = new Ball(bar);   // �{�[��
    score = new Score();  // �X�R�A
    complete = false;

    // �L�[���X�i�[�ǉ�
    addKeyListener(this);
  }

  /*
   * �J�n--------------------------------------------------------------------------------------------------------------------------
   */
  public void start(){
    if(runner==null){
      runner=new Thread(this);
      runner.start();
    }
  }

  /*
   * ��~-------------------------------------------------------------------------------------------------------------------------------
   */
  public void stop(){
    runner=null;
  }

  /*
   * ���s----------------------------------------------------------------------------------------------------------------------------
   */
  public void run(){
    makeBlocks();
    while(runner!=null){
      repaint();
      ball.move(bar, blocks, score);     // �{�[���̓���i�o�[, �u���b�N�̔z���n���j
      if(complete)  break;
      try{
        runner.sleep(10);
      }
      catch(InterruptedException e){}
    }
  }

  /*
   * �L�[���X�i�[-----------------------------------------------------------------------------------------------------------------
   */
  public void keyPressed(KeyEvent ev){
    switch(ev.getKeyCode()){
    // �o�[�̈ړ�
    case KeyEvent.VK_LEFT:  bar.moveLeft(); break;
    case KeyEvent.VK_RIGHT: bar.moveRight(); break;
    case KeyEvent.VK_SPACE: ball.moveFlag = true; break;
    case KeyEvent.VK_C: ball.init(); break;
    }
  }
  public void keyReleased(KeyEvent ev){
      switch(ev.getKeyCode()){
      case KeyEvent.VK_LEFT:  bar.dx = bar.dx_default; bar.lFlag = false; break;
      case KeyEvent.VK_RIGHT: bar.dx = bar.dx_default; bar.rFlag = false; break;
      case KeyEvent.VK_SPACE: break;
      case KeyEvent.VK_C: break;
    }
  }
  public void keyTyped(KeyEvent ev){}

  /*
   * �u���b�N�̍쐬-----------------------------------------------------------------------------------------------
   */
  public void makeBlocks(){
    for(int i=0; i<8; i++){
      // �s���ƂɃ����_���ŐF����
      Color col = new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
      for(int j=0; j<5; j++)
        blocks.addElement(new Block(width/12+j*Block.x_len, height/30+i*Block.y_len, col));
    }
  }

  /*
   * �`��--------------------------------------------------------------------------------------------------------------------------------------
   */
  public void paint(Graphics g){
    try{
      offg.clearRect(0,0,width,height);
      offg.drawImage( backGroundImage , 0 , 0 ,width,height, this );
      bar.draw(offg);       // �o�[��`��
      ball.draw(offg);        // �{�[����`��
      count=0;              // �J�E���g������
      for(Block block:blocks){     // �u���b�N�����݂���Ε`�悷��
        if(block.exist) block.draw(offg);
        if(!block.exist) count++;       // �����ꂽ�u���b�N�̐����J�E���g
      }
      if(count == 40){
        complete = true;    //*************�u���b�N�����ׂĂȂ���΃N���A********//
        offg.drawImage( questclear_t , 0 , height/4 ,          // �A�X�y�N�g����ێ������܂܏k��
            width,questclear_t.getHeight(null)*width/questclear_t.getWidth(null), this );
      }
      drawScore(g);           // �X�R�A�̕`��
      g.drawImage(off,0,0,this);
    }
    catch(NullPointerException e) {}
  }

  public void drawScore(Graphics g){
    // �X�R�A�`��
    offg.setFont(new Font("Aries", Font.ITALIC, 30));    // �t�H���g�ݒ�
    String scoreS = "Score: " + String.valueOf(score.score);     // �X�R�A�ݒ�
    int stringSize = offg.getFontMetrics().stringWidth(scoreS);      // �t�H���g�̕����擾
    offg.drawString(scoreS, width - stringSize, height);     // �w�肵�����W�ɉE�l�ŕ`��

    // �`�F�C���`��
    offg.setFont(new Font("MS Gothic", Font.PLAIN, 20));
    String blockChainS = "�A�����ː�: " + ball.barChain;
    String barChainS = "�A����: " + ball.blockChain;
    offg.drawString(blockChainS, 0, height - offg.getFontMetrics().getHeight());
    offg.drawString(barChainS, 0, height - 2);

    // 1�u���b�N�X�R�A�\��
    String blockScore = " 1�u���b�N�̃|�C���g: "
        + (int)(10 + ball.barChain + ball.blockChain*2 + Math.abs(ball.dx)/3);
    offg.drawString(blockScore, 0, offg.getFontMetrics().getHeight());
  }

  // �o�b�t�@�C���[�W�쐬
  public BufferedImage createBufferedImage(Image img) {
    BufferedImage bimg =
        new BufferedImage( img.getWidth(null), img.getHeight(null),  BufferedImage.TYPE_INT_ARGB);
    Graphics g = bimg.getGraphics();
    g.drawImage(img, 0, 0, null);
    g.dispose();
    return bimg;
}

  // ���ߏ���
  public Image getTransparentImage( BufferedImage buf ){
    int tc = buf.getRGB(0,0);
    for ( int y = 0; y<buf.getHeight(); y++ ){
      for ( int x = 0; x <buf.getWidth(); x++ ){
        if ( buf.getRGB(x,y) == tc ){
          buf.setRGB(x,y,tc & 0x00ffffff);
        }
      }
    }
    return (Image)buf;
  }

  public void update(Graphics g){ paint(g); }

  /*
   * �A�v���p----------------------------------------------------------------------------------------------------------------------
   */
  public static void main(String[] args){
    Main applet = new Main();
    Frame f = new Frame();
    f.setSize( 500, 900 );
    f.add( applet );
    f.setVisible( true );
    applet.init();
    applet.start();
    f.addWindowListener( new WindowEventHandler() );
  }
}

// �A�v���P�[�V�����p�̃E�C���h�E���X�i�[------------------------------------------------------------------------------------
class WindowEventHandler extends WindowAdapter {
  public void windowClosing( WindowEvent ev ){
    System.exit(0);
  }
}