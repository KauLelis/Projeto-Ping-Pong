import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PongDosCria extends JFrame {
    public PongDosCria() {
        setTitle("PONG DOS CRIA"); // TÍTULO
        setSize(720, 639); // TAMANHO DA JANELA
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // DIZ QUE O BOTÃO DE "FECHAR" FINALIZA O GAME
        setLocationRelativeTo(null); // CENTRALIZA A JANELA
        setResizable(false); // IMPEDE QUE O USUÁRIO REDIMENSIONE A JANELA

        // ADICIONANDO O PAINEL DO JOGO À JANELA PRINCIPAL
        PainelPong painelPong = new PainelPong();
        add(painelPong);

        // INICIA O LOOP DO JOGO
        painelPong.startGameLoop();
    }

    // ESSE TRECHO DE CÓDIGO GARANTE QUE A JANELA DO JOGO SEJA CRIADA E TORNADA
    // VISÍVEL NA THREAD DE DESPACHO DE EVENTOS DA GUI
    // GARANTINDO ASSIM A CORRETA ATUALIZAÇÃO DA INTERFACE GRÁFICA DO USUÁRIO
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PongDosCria game = new PongDosCria();
            game.setVisible(true);
        });
    }
}

class PainelPong extends JPanel implements KeyListener {

    // DEFININDO CONSTANTES PARA AS DIMENSÕES DO CAMPO DE JOGO
    private static final int WIDTH = 705;
    private static final int HEIGHT = 600;

    private int ballX = WIDTH / 2; // POSIÇÃO INICIAL X DA BOLINHA
    private int ballY = HEIGHT / 2; // POSIÇÃO INICIAL Y DA BOLINHA
    private int ballDiameter = 20; // DIÂMETRO DA BOLINHA

    private int paddleWidth = 10; // LARGURA DAS RAQUETES
    private int paddleHeigth = 80; // ALTURA DAS RAQUETES

    // VARIÁVEIS QUE VÃO DETERMINAR A VELOCIDADE DA BOLA

    private int ballSpeedX = 2; // VELOCIDADE DA BOLINHA NO EIXO X
    private int ballSpeedY = 2; // VELOCIDADE DA BOLINHA NO EIXO Y

    // VARIÁVEL QUE VAI DETERMINAR A VELOCIDADE DA RAQUETE
    private int paddleSpeed = 7; // VELOCIDADE DAS RAQUETES

    // VARIÁVEIS QUE VÃO RASTREAR O ESTADO DAS TECLAS PRESSIONADAS
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    // VARIÁVEL PARA CONTROLAR A POSIÇÃO VERTICAL DA RAQUETE ESQUERDA
    private int raqueteEsquerdaY = HEIGHT / 2 - paddleHeigth / 2;

    // VARIÁVEL PARA CONTROLAR A POSIÇÃO VERTICAL DA RAQUETE DIREITA
    private int raqueteDireitaY = HEIGHT / 2 - paddleHeigth / 2;

    // CONSTRUTOR
    public PainelPong() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true); // PERMITE QUE O PAINEL RECEBA FOCO PARA OUVIR EVENTOS DO TECLADO
        addKeyListener(this); // ADICIONA ESTE PAINEL COMO OUVINTE DE TECLADO
    }

    // SOBRESCREVENDO O MÉTODO "paintComponent" PARA DESENHAR OS ELEMENTOS DO JOGO
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // DESENHANDO A BOLINHA
        g.setColor(Color.WHITE);
        g.fillOval(ballX - ballDiameter / 2, ballY - ballDiameter / 2, ballDiameter, ballDiameter);

        // DESENHANDO AS RAQUETES
        g.fillRect(20, raqueteEsquerdaY, paddleWidth, paddleHeigth); // RAQUETE DA ESQUERDA
        g.fillRect(WIDTH - 30, raqueteDireitaY, paddleWidth, paddleHeigth); // RAQUETE DA DIREITA
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode(); // CAPTURA A TECLA PRESSIONADA

        if (keyCode == KeyEvent.VK_W) {
            wPressed = true;
        } else if (keyCode == KeyEvent.VK_S) {
            sPressed = true;
        } else if (keyCode == KeyEvent.VK_UP) {
            upPressed = true;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            downPressed = true;
        }

        if (keyCode == KeyEvent.VK_W) {
            // MOVIMENTA A RAQUETE PRA CIMA
            if (raqueteEsquerdaY > 0) { // VERIFICA SE A RAQUETE NÃO ESTÁ NA BORDA SUPERIOR
                raqueteEsquerdaY -= paddleSpeed; // MOVE A RAQUETE PARA CIMA
            }
        } else if (keyCode == KeyEvent.VK_S) {
            // MOVIMENTA A RAQUETE PRA BAIXO
            if (raqueteEsquerdaY + paddleHeigth < HEIGHT) { // VERIFICA SE A RAQUETE NÃO ESTÁ NA BORDA INFERIOR
                raqueteEsquerdaY += paddleSpeed; // MOVE A RAQUETE PARA BAIXO
            }
        }
        if (keyCode == KeyEvent.VK_UP) {
            // MOVIMENTA A RAQUETE PRA CIMA
            if (raqueteDireitaY > 0) { // VERIFICA SE A RAQUETE NÃO ESTÁ NA BORDA SUPERIOR
                raqueteDireitaY -= paddleSpeed; // MOVE A RAQUETE PARA CIMA
            }
        } else if (keyCode == KeyEvent.VK_DOWN) {
            // MOVIMENTA A RAQUETE PRA BAIXO
            if (raqueteDireitaY + paddleHeigth < HEIGHT) { // VERIFICA SE A RAQUETE NÃO ESTÁ NA BORDA INFERIOR
                raqueteDireitaY += paddleSpeed; // MOVE A RAQUETE PARA BAIXO
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // MÉTODO OBRIGATÓRIO PARA IMPLEMENTAR A INTERFACE "KeyListener"
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // MÉTODO OBRIGATÓRIO PARA IMPLEMENTAR A INTERFACE "KeyListener"
    }

    public void startGameLoop() {
        Thread gameLoop = new Thread(() -> {
            while (true) {
                // AQUI ESTÁ ATUALIZANDO A POSIÇÃO DA BOLA
                ballX += ballSpeedX;
                ballY += ballSpeedY;

                if (ballX <= 0) {
                    ballX = WIDTH / 2;
                    ballY = HEIGHT / 2;
                    ballSpeedX = -ballSpeedX;
                    ballSpeedY = -ballSpeedY;

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (ballX >= WIDTH - ballDiameter) {
                    ballX = WIDTH / 2;
                    ballY = HEIGHT / 2;
                    ballSpeedX = -ballSpeedX;
                    ballSpeedY = -ballSpeedY;

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // VERIFICANDO COLISÕES COM AS RAQUETES
                if ((ballX <= 20 + paddleWidth
                        && ballY >= raqueteEsquerdaY
                        && ballY <= raqueteEsquerdaY + paddleHeigth)
                        ||
                        (ballX >= WIDTH - 30 - paddleWidth
                                && ballY + ballDiameter / 3 >= raqueteDireitaY
                                && ballY - ballDiameter / 3 <= raqueteDireitaY + paddleHeigth)) {

                    ballSpeedX = -ballSpeedX; // INVERTE A DIREÇÃO DO EIXO X
                }

                // AQUI VERIFICA AS COLISÕES COM AS BORDAS DA TELA E INVERTE A DIRECEÇÃO SE
                // NECESSÁRIO
                if (ballX <= 0) {
                    ballX = 0;
                    ballSpeedX = -ballSpeedX;
                } else if (ballX >= WIDTH - ballDiameter) {
                    ballX = WIDTH - ballDiameter;
                    ballSpeedX = -ballSpeedX; // INVERTENDO A DIREÇÃO NO EIXO X, NO CASO: (PRA FRENTE OU PRA TRÁS)
                }

                // LIMITA A POSIÇÃO DA BOLA PARA QUE ELA NÃO SAIA DA TELA
                if (ballY <= 0) {
                    ballY = 0;
                    ballSpeedY = -ballSpeedY;
                } else if (ballY >= HEIGHT - ballDiameter) {
                    ballY = HEIGHT - ballDiameter;
                    ballSpeedY = -ballSpeedY; // INVERTENDO A DIREÇÃO NO EIXO Y, NO CASO: (PRA CIMA OU PRA BAIXO)
                }

                // REPINTA O PAINEL PARA ATUALIZAR A TELA
                repaint();

                // AGUARDA UM CURTO INTERVALO DE TEMPO PARA CONTRAR A TAXA DE ATUALIZAÇÃO
                try {
                    Thread.sleep(10); // 10 MILISSEGUNDOS (AJUSTÁVEL)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameLoop.start();
    }
}
