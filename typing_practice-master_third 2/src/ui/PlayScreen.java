package ui;

import txt.TxtReader;
import util.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

// PlayScreen 클래스는 GUI 기반의 타자 연습 플레이 화면을 띄움
public class PlayScreen extends JFrame {
    private JTextField[] sentenceFields; // 문장 출력
    private JTextField[] inputFields; // 사용자 입력
    private JTextField accuracyField; // 정확도 표시
    private JTextField currentTypingSpeedField; // 현재 타수 표시
    private JTextField timeField; // 경과 시간 표시
    private List<String> lines; // 텍스트 파일에서 읽은 문장 리스트
    private int currentIndex = 0; // 현재 문장의 인덱스
    private Calculator calculator; // 정확도와 속도 계산
    private Timer timer; // 경과 시간을 측정하기 위한 타이머


    // 전달받은 파일 번호를 바탕으로 해당 파일의 내용을 읽고 GUI를 초기화
    public PlayScreen(int fileNum) {
        try {
            TxtReader txtReader = new TxtReader();
            this.lines = txtReader.txtRead(fileNum); // 파일에서 문장을 읽어옵니다.
            this.calculator = new Calculator();
            createGUI(); // GUI 생성
            updateSentences(); // 초기 문장 설정
            startTimers(); // 타이머 시작
            calculator.start(); // 계산기 시작
            inputFields[0].requestFocusInWindow(); // 첫 번째 입력 필드에 포커스 설정
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "파일을 읽는 중 오류가 발생했습니다."); // 오류 메시지를 표시합니다.
            this.dispose(); // 창을 닫습니다.
        }
    }

    // GUI를 생성하는 메서드입니다.
    private void createGUI() {
        setTitle("타자 연습 프로그램");                                       // 제목
        setSize(800, 600);                                  // 사이즈
        setResizable(false);                                             // 화면 사이즈 변경 불가
        setLocationRelativeTo(null);                                     // 화면 중앙에 창 배치
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                  // 창 끄면 프로그램 종료
        setLayout(new BorderLayout());                                   // BorderLayout 설정
        JPanel infoPanel = new JPanel(new GridLayout(1, 4));  // infoPanel의 레이아웃을 GridLayout으로 설정
        JButton backButton = new JButton("뒤로가기");                 // 뒤로가기 버튼 생성
        backButton.addActionListener(e -> {                              // 뒤로가기 버튼에 메인 화면으로 이동 기능 추가
            timer.stop();
            new MainScreen().setVisible(true);
            this.dispose();
        });
        accuracyField = new JTextField("정확도: 0%");                      // 정확도
        currentTypingSpeedField = new JTextField("현재 타수: 0타/분");       // 타자속도
        timeField = new JTextField("시간: 0초");                           // 시간
        accuracyField.setEditable(false);                                 // 정확도 사용자가 수정할 수 없게 설정
        currentTypingSpeedField.setEditable(false);                       // 타자속도 사용자가 수정할 수 없게 설정
        timeField.setEditable(false);                                     // 타이머 사용자가 수정할 수 없게 설정
        infoPanel.add(backButton);
        infoPanel.add(accuracyField);
        infoPanel.add(currentTypingSpeedField); /* infoPanel에 뒤로가기 버튼, 정확도, 타자속도, 시간 영역 추가 */
        infoPanel.add(timeField);
        add(infoPanel, BorderLayout.NORTH);                  // infoPanel을 북쪽에 위치 시킴
        JPanel contentPanel = new JPanel(new GridLayout(10, 1, 5, 5));  // 컨텐츠를 띄울 영역을 그리드 레이아웃을 이용해서 1행 10열짜리로 만듬
        sentenceFields = new JTextField[5];  // 배열 생성
        inputFields = new JTextField[5];     // 배열 생성
        for (int i = 0; i < 5; i++) {
            sentenceFields[i] = new JTextField("문장 출력창");
            inputFields[i] = new JTextField();
            sentenceFields[i].setEditable(false);  // 사용자가 보고 따라쳐야 하는 문장은 수정 못 하게 막음
            int index = i;
            inputFields[i].addKeyListener(new KeyAdapter() {                    // 각 입력 필드에 키 리스너를 추가하여 엔터 키 입력 시 다음 필드로 포커스를 이동합니다.
                @Override
                public void keyReleased(KeyEvent e) {
                    String userInput = inputFields[index].getText();
                    String expectedSentence = lines.get(currentIndex + index);
                    if (userInput.length() > expectedSentence.length()) {    // 사용자가 입력한 문자열의 길이가 예상되는 문자열의 길이를 초과할 경우
                        if (index + 1 < inputFields.length && currentIndex + index + 1 < lines.size()) {
                            inputFields[index + 1].requestFocus(); // 다음 입력창으로 커서 옮김
                        } else {
                            processInputs(); // 모든 입력창에 입력이 완료되면 다음 문장 세트를 채움
                        }
                    } else {
                        updateStats(); // 입력이 변경될 때마다 업데이트합니다.
                        System.out.println("출력");
                    }
                }
            });
            contentPanel.add(sentenceFields[i]);
            contentPanel.add(inputFields[i]);
        }
        add(contentPanel, BorderLayout.CENTER);  // contentPanel 을 중앙에 위치시킴
    }
    private void updateSentences() {                // 현재 인덱스에 맞춰 문장을 업데이트하는 메서드
        for (int i = 0; i < 5; i++) {
            if (currentIndex + i < lines.size()) {             // currentIndex는 현재 문장, lines 는 txt 파일을 읽어온 내용
                sentenceFields[i].setText(lines.get(currentIndex + i));
                inputFields[i].setText("");
            } else {
                sentenceFields[i].setVisible(false);
                inputFields[i].setVisible(false);
            }
        }
        inputFields[0].requestFocusInWindow();
    }

    // 타이머를 시작하여 주기적으로 통계를 업데이트합니다.
    private void startTimers() {
        timer = new Timer(1000, e -> {
            calculator.updateElapsedTime();
            timeField.setText("시간: " + calculator.getElapsedTime() + "초");
            accuracyField.setText(String.format("정확도: %.2f%%", calculator.getAccuracy()));
            currentTypingSpeedField.setText("현재 타수: " + calculator.getTypingSpeed() + "타/분");
        });
        timer.start();
    }
    // 모든 입력창에 입력이 완료되면 다음 문장 세트를 채움
    private void processInputs() {
        int tempCorrectChars = 0;
        int tempTotalInputChars = 0;
        for (int i = 0; i < inputFields.length; i++) {
            if (currentIndex + i < lines.size()) {
                String expected = lines.get(currentIndex + i);
                String userInput = inputFields[i].getText().stripTrailing();
                tempCorrectChars += calculateCorrectChars(expected, userInput);
                tempTotalInputChars += userInput.length();
            }
        }
        calculator.updateOldStats(tempCorrectChars, tempTotalInputChars);
        currentIndex += inputFields.length; // 다음 문장 세트로 이동
        if (currentIndex >= lines.size()) {
            timer.stop();
            calculator.updateElapsedTime();
            showResults();
        } else {
            updateSentences();
        }
    }

    private void updateStats() {
        int tempCorrectChars = Calculator.oldCorrectChars;
        int tempTotalInputChars = Calculator.oldTotalInputChars;
        // 각 입력 필드의 데이터를 기반으로 계산
        for (int i = 0; i < inputFields.length; i++) {
            if (currentIndex + i < lines.size()) {
                String expected = lines.get(currentIndex + i);
                String userInput = inputFields[i].getText().stripTrailing();

                tempCorrectChars += calculateCorrectChars(expected, userInput);
                tempTotalInputChars += userInput.length();
            }
        }
        calculator.updateStats(tempCorrectChars, tempTotalInputChars);
        accuracyField.setText(String.format("정확도: %.2f%%", calculator.getAccuracy()));
        currentTypingSpeedField.setText("현재 타수: " + calculator.getTypingSpeed() + "타/분");
    }

    // 예상된 문자열과 사용자 입력 문자열을 비교하여 맞춘 글자 수를 계산합니다.
    private int calculateCorrectChars(String expected, String userInput) {
        int correct = 0;
        for (int i = 0; i < Math.min(expected.length(), userInput.length()); i++) {
            if (expected.charAt(i) == userInput.charAt(i)) {
                correct++;
            }
        }
        return correct;
    }

    // 결과를 보여주는 메서드입니다. 최종 정확도와 속도를 ResultScreen에 전달합니다.
    private void showResults() {
        long totalTime = calculator.getElapsedTime();
        double finalAccuracy = calculator.getAccuracy();
        int finalTypingSpeed = calculator.getTypingSpeed();

        new ResultScreen((int) totalTime, finalAccuracy, finalTypingSpeed).setVisible(true);
        this.dispose();
    }
}
