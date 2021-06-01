```
public class ClipBuddy {

    public static final Path PATH = Path.of("notes.md");
    private static String oldData = "";

    public static void main(String[] args) throws IOException, UnsupportedFlavorException, InterruptedException {
        clearClipboard();
        while (true) {
            readFromClipboard();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static void readFromClipboard() throws IOException, UnsupportedFlavorException {
        var defaultToolkit = Toolkit.getDefaultToolkit();
        var systemClipboard = defaultToolkit.getSystemClipboard();
        var dataFlavorAvailable = systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor);

        if (dataFlavorAvailable) {
            var data = systemClipboard.getData(DataFlavor.stringFlavor);
            if (data instanceof String s) {
                var normalizedText = TextCleaner.normalizeText(s);

                if (!oldData.equals(normalizedText)) {
                    System.out.println(normalizedText);
                    Files.writeString(PATH, normalizedText+ "\n\n", StandardOpenOption.APPEND);
                    oldData = normalizedText;
                }
            }
        }
    }

    private static void clearClipboard() {
        var defaultToolkit = Toolkit.getDefaultToolkit();
        var systemClipboard = defaultToolkit.getSystemClipboard();
        systemClipboard.setContents(new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[0];
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return false;
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return null;
            }
        }, null);
    }
}
```


```
public class TextCleaner {

    public static String normalizeText(String text) {
        var finalText = new StringBuilder();
        var lines = text.split("\n");
        for (var line : lines) {
            var noSpaces = StringUtils.normalizeSpace(line).strip();
            if (noSpaces.endsWith("-")) {
                var joinWord = noSpaces.substring(0, noSpaces.length() - 1);
                finalText.append(joinWord);
            } else if (noSpaces.startsWith("")) {
                finalText.append(noSpaces.replace("", "\n*")).append(" ");
            } else if (noSpaces.endsWith(".PNG") || noSpaces.endsWith(".png")) {
                finalText.append("![").append(noSpaces).append("]").append("(pictures/").append(noSpaces).append(")");
            } else {
                finalText.append(noSpaces).append(" ");
            }
        }
        return finalText.toString().strip();
    }
}
```
