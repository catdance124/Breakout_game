/***
 *** ブロック崩しメインソース
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

  Thread runner;    // 実行用スレッド
  int width,height;   // アプレットの大きさ
  Image off;      // 裏描画用オフスクリーンイメージ
  Image backGroundImage;  // 背景
  Image questclear, questclear_t;
  BufferedImage bufquestclear;
  Graphics offg;      // そのグラフィックス
  Vector <Block> blocks;      // 可変長配列
  Bar bar;      // バーのインスタンス
  Ball ball;      // ボールのインスタンス
  Score score;      // スコアのインスタンス
  boolean complete;   //クリアフラグ
  int count;            // 消えたブロックカウント用

  /*
   * 初期化--------------------------------------------------------------------------------------------------------------------
   */
  public void init(){
    // 背景設定
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

    // ブロック配列
    blocks=new Vector<Block>();

    // オフスクリーンイメージの取得
    width=getSize().width;
    height=getSize().height;
    off=createImage(width,height);
    offg=off.getGraphics();

    // サイズ設定
    bar.setSize(width,height);
    ball.setSize(width,height);
    Block.setSize(width,height);

    bar = new Bar();    // バー
    ball = new Ball(bar);   // ボール
    score = new Score();  // スコア
    complete = false;

    // キーリスナー追加
    addKeyListener(this);
  }

  /*
   * 開始--------------------------------------------------------------------------------------------------------------------------
   */
  public void start(){
    if(runner==null){
      runner=new Thread(this);
      runner.start();
    }
  }

  /*
   * 停止-------------------------------------------------------------------------------------------------------------------------------
   */
  public void stop(){
    runner=null;
  }

  /*
   * 実行----------------------------------------------------------------------------------------------------------------------------
   */
  public void run(){
    makeBlocks();
    while(runner!=null){
      repaint();
      ball.move(bar, blocks, score);     // ボールの動作（バー, ブロックの配列を渡す）
      if(complete)  break;
      try{
        runner.sleep(10);
      }
      catch(InterruptedException e){}
    }
  }

  /*
   * キーリスナー-----------------------------------------------------------------------------------------------------------------
   */
  public void keyPressed(KeyEvent ev){
    switch(ev.getKeyCode()){
    // バーの移動
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
   * ブロックの作成-----------------------------------------------------------------------------------------------
   */
  public void makeBlocks(){
    for(int i=0; i<8; i++){
      // 行ごとにランダムで色決定
      Color col = new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
      for(int j=0; j<5; j++)
        blocks.addElement(new Block(width/12+j*Block.x_len, height/30+i*Block.y_len, col));
    }
  }

  /*
   * 描画--------------------------------------------------------------------------------------------------------------------------------------
   */
  public void paint(Graphics g){
    try{
      offg.clearRect(0,0,width,height);
      offg.drawImage( backGroundImage , 0 , 0 ,width,height, this );
      bar.draw(offg);       // バーを描画
      ball.draw(offg);        // ボールを描画
      count=0;              // カウント初期化
      for(Block block:blocks){     // ブロックが存在すれば描画する
        if(block.exist) block.draw(offg);
        if(!block.exist) count++;       // 消されたブロックの数をカウント
      }
      if(count == 40){
        complete = true;    //*************ブロックがすべてなければクリア********//
        offg.drawImage( questclear_t , 0 , height/4 ,          // アスペクト比を維持したまま縮小
            width,questclear_t.getHeight(null)*width/questclear_t.getWidth(null), this );
      }
      drawScore(g);           // スコアの描画
      g.drawImage(off,0,0,this);
    }
    catch(NullPointerException e) {}
  }

  public void drawScore(Graphics g){
    // スコア描画
    offg.setFont(new Font("Aries", Font.ITALIC, 30));    // フォント設定
    String scoreS = "Score: " + String.valueOf(score.score);     // スコア設定
    int stringSize = offg.getFontMetrics().stringWidth(scoreS);      // フォントの幅を取得
    offg.drawString(scoreS, width - stringSize, height);     // 指定した座標に右詰で描画

    // チェイン描画
    offg.setFont(new Font("MS Gothic", Font.PLAIN, 20));
    String blockChainS = "連続反射数: " + ball.barChain;
    String barChainS = "連鎖数: " + ball.blockChain;
    offg.drawString(blockChainS, 0, height - offg.getFontMetrics().getHeight());
    offg.drawString(barChainS, 0, height - 2);

    // 1ブロックスコア表示
    String blockScore = " 1ブロックのポイント: "
        + (int)(10 + ball.barChain + ball.blockChain*2 + Math.abs(ball.dx)/3);
    offg.drawString(blockScore, 0, offg.getFontMetrics().getHeight());
  }

  // バッファイメージ作成
  public BufferedImage createBufferedImage(Image img) {
    BufferedImage bimg =
        new BufferedImage( img.getWidth(null), img.getHeight(null),  BufferedImage.TYPE_INT_ARGB);
    Graphics g = bimg.getGraphics();
    g.drawImage(img, 0, 0, null);
    g.dispose();
    return bimg;
}

  // 透過処理
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
   * アプリ用----------------------------------------------------------------------------------------------------------------------
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

// アプリケーション用のウインドウリスナー------------------------------------------------------------------------------------
class WindowEventHandler extends WindowAdapter {
  public void windowClosing( WindowEvent ev ){
    System.exit(0);
  }
}