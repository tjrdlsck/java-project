package ui;

// GUI 구성 요소와 이벤트 처리를 위한 클래스 임포트
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// MainScreen 클래스는 창을 생성
public class MainScreen extends JFrame {
    // 선택된 텍스트 번호를 저장하는 변수
    private int selectedTextNumber = 1;

    // createGUI() 호출 - GUI 구성 요소를 설정하는 메서드 불러옴
    public MainScreen() {
        createGUI();
    }


    public static void main(String[] args) {
        // GUI를 안전하게 초기화하기 위해 사용되는 메서드
        SwingUtilities.invokeLater(MainScreen::new);
    }

    // GUI 구성 요소를 설정하는 메서드
    private void createGUI() {
        // 창 제목, 크기 및 기본 닫기 작업 설정
        setTitle("타자 연습 프로그램");
        setSize(800, 600);
        setResizable(false); // 창 크기 조정 불가능하도록 설정
        setLocationRelativeTo(null); // 화면 중앙에 창 배치
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // x표시 창 끄는 기능

        // 수직 박스 레이아웃과 흰색 배경을 가진 메인 패널 생성
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // 수직 방향 정렬
        mainPanel.setBackground(Color.WHITE); // 흰색 배경

        // 제목을 위한 레이블 생성 및 속성 설정
        JLabel mainLabel = new JLabel("타자 연습 프로그램");
        mainLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 레이블 중앙 정렬
        mainLabel.setFont(new Font("돋움", Font.BOLD, 36)); // 폰트 스타일 및 크기 설정
        mainLabel.setForeground(Color.BLACK); // 텍스트 색상 설정

        // 모드 선택 및 게임 시작 버튼 생성
        JButton modeButton = createButton("글 선택", e -> onModeButtonClick());  // 버튼에 "글 선택"을 띄우고 이벤트 속성 추가
        JButton startButton = createButton("게임 시작", e -> onStartButtonClick()); // 버튼에 "게임 시작"을 띄우고 이벤트 속성 추가

        // 컴포넌트를 메인 패널에 추가하고 사이 간격 추가
        mainPanel.add(Box.createVerticalStrut(100)); // 수직 방향으로 여백 생성
        mainPanel.add(mainLabel);                           // 버튼 생성
        mainPanel.add(Box.createVerticalStrut(150)); // 수직 방향으로 여백 생성
        mainPanel.add(modeButton);                          // 버튼 생성
        mainPanel.add(Box.createVerticalStrut(30));  // 수직 방향으로 여백 생성
        mainPanel.add(startButton);                         // 버튼 생성

        // 프레임에 mainPanel 추가
        add(mainPanel);

        // 프레임을 보이도록 설정
        setVisible(true);
    }

    // 지정된 텍스트와 action 매개변수를 추가해서 버튼을 생성하는 메서드
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("돋움", Font.PLAIN, 18)); // 텍스트 폰트 스타일 및 크기 설정
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬
        button.setMaximumSize(new Dimension(200, 50)); // 버튼의 최대 크기 설정
        button.addActionListener(action); // 버튼 클릭 시 실행할 action 추가
        return button;
    }

    // 모드 선택 버튼 클릭 시 호출되는 메서드
    private void onModeButtonClick() {
        String[] options = {"1. 메밀꽃 필무렵", "2. 별헤는 밤", "3. 애국가", "4. test"}; // 텍스트 선택 옵션 기능을 위해 저장한 리스트

        // 미리 정의된 선택 항목 중 텍스트 옵션을 선택하기 위해 입력 대화상자 표시
        String selectedOption = (String) JOptionPane.showInputDialog(
                this,           // MainScreen을 가르키는 this - 대화상자는 MainScreen 창의 중앙에 표시됨
                "글을 선택하세요:",                // 대화상자의 프롬프트 메시지
                "글 선택",                       // 대화상자의 제목
                JOptionPane.QUESTION_MESSAGE,  // 메시지 대화상자의 유형 (질문)
                null,                          // 아이콘 설정
                options,                       // option 배열 불러옴
                options[0]                     // 대화상자의 기본 선택 옵션
        );

        if (selectedOption != null) {
            selectedTextNumber = Integer.parseInt(selectedOption.split("\\.")[0]); // "."을 기준으로 분리하여 옵션의 숫자만 저장해둠
            JOptionPane.showMessageDialog(this, selectedOption + "을(를) 선택하셨습니다.");
            // 선택한 옵션을 확인하는 메시지 대화상자 표시
        }
    }

    private void onStartButtonClick() {
        PlayScreen playScreen = new PlayScreen(selectedTextNumber); // 저장했던 번호를 PlayScreen에 넘겨줌
        playScreen.setVisible(true);
        this.dispose();
         /* 선택된 텍스트 번호로 PlayScreen 생성
            이를 보이도록 하고 MainScreen은 닫음 */
    }

}
