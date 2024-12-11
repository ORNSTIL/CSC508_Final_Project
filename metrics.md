# Project Metrics Report

This report provides detailed metrics for each file in the project. The metrics include:
- **Instability (I)**: Measures the tendency of a class to change due to changes in other classes.
- **Abstractness (A)**: Measures the level of abstraction in a class.
- **Distance from Main Sequence (D)**: A score calculated as \( |D = A + I - 1| \).
- **Cyclomatic Complexity (CC)**: A measure of the complexity of each method in a class.

## File: `Blackboard.java`

### Class: `Blackboard`
- **Instability (I)**: 0
- **Abstractness (A)**: 0 (concrete class with no abstract methods).
- **Distance from Main Sequence (D)**: \( |D = 0 + 0 - 1| = 1 \).

#### Cyclomatic Complexity
- `getInstance()`: CC = 2
- `addPropertyChangeListener(PropertyChangeListener listener)`: CC = 1
- `removePropertyChangeListener(PropertyChangeListener listener)`: CC = 1
- `setKeyboardLetter(String keyboardLetter)`: CC = 2
- `getKeyboardLetter()`: CC = 1
- `clearCobotCommands()`: CC = 1
- `addToCobotCommands(String newCommand)`: CC = 1
- `removeFromCobotCommands()`: CC = 1

---

## File: `Cobot.java`

### Class: `Cobot`
- **Instability (I)**: 0.67
- **Abstractness (A)**: 0 (concrete class).
- **Distance from Main Sequence (D)**: \( |D = 0 + 0.67 - 1| = 0.33 \).

#### Cyclomatic Complexity
- `connect()`: CC = 1
- `disconnect()`: CC = 3
- `isConnected()`: CC = 1
- `sendCommand(String command)`: CC = 2
- `propertyChange(PropertyChangeEvent evt)`: CC = 2

---

## File: `CobotControlPanel.java`

### Class: `CobotControlPanel`
- **Instability (I)**: 0.67
- **Abstractness (A)**: 0 (concrete class).
- **Distance from Main Sequence (D)**: \( |D = 0 + 0.67 - 1| = 0.33 \).

#### Cyclomatic Complexity
- `connectCobot(ActionEvent e)`: CC = 2
- `disconnectCobot(ActionEvent e)`: CC = 1
- `handleKeyPress(ActionEvent e)`: CC = 3

---

## File: `CobotGUI.java`

### Class: `CobotGUI`
- **Instability (I)**: CC = 0.5
- **Abstractness (A)**: 0 (concrete class).
- **Distance from Main Sequence (D)**: \( |D = 0 + 0.5 - 1| = 0.5 \).

#### Cyclomatic Complexity
- `createHeaderPanel(ActionListener connectAction, ActionListener disconnectAction)`: CC = 1
- `createKeyboardPanel(ActionListener keyPressAction)`: CC = 1
- `addKeyboardRow(JPanel parentPanel, String[] keys, ActionListener keyPressAction)`: CC = 2
- `createKeyButton(String key, ActionListener keyPressAction)`: CC = 1
- `createStyledButton(String text, ActionListener action)`: CC = 1

---

## File: `CobotDrawLetterCommandGenerator.java`

### Class: `CobotDrawLetterCommandGenerator`
- **Instability (I)**: 0.67
- **Abstractness (A)**: 0 (concrete class).
- **Distance from Main Sequence (D)**: \( |D = 0 + 0.67 - 1| = 0.33 \).

#### Cyclomatic Complexity
- `propertyChange(PropertyChangeEvent evt)`: CC = 3
- `genLetter(String letter)`: CC = 3
- `clampAdjustNumber(double val, double originalLim, double targetLim)`: CC = 3

---

## File: `LetterDataProcessor.java`

### Class: `LetterDataProcessor`
- **Instability (I)**: 0
- **Abstractness (A)**: 0 (concrete class).
- **Distance from Main Sequence (D)**: \( |D = 0 + 0 - 1| = 1 \).

#### Cyclomatic Complexity
- `LetterDataProcessor(String datafile, int startIndex)`: CC = 4
- `getLetterLines(String letter)`: CC = 1

---

## File: `Main.java`

### Class: `Main`
- **Instability (I)**: 1.
- **Abstractness (A)**: 0 (concrete class).
- **Distance from Main Sequence (D)**: \( |D = 0 + 1 - 1| = 0 \).

#### Cyclomatic Complexity
- `main(String[] args)`: CC = 1 

---

## File: `ServerConfig.java`

### Class: `ServerConfig`
- **Instability (I)**: 0 
- **Abstractness (A)**: 1 (entirely abstract interface using an Enum).
- **Distance from Main Sequence (D)**: \( |D = 1 + 0 - 1| = 0 \).

#### Cyclomatic Complexity
- Enum methods (`getHost()`, `getPort()`): CC = 1 (simple getters).

