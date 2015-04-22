pushd C:/Users/Jesse Boyd/workspace/PuzzleGen/files/
for %%F in ("/Users/Jesse Boyd/workspace/PuzzleGen/files/*.txt") do (
   java com.boydti.puzzle.Main "/Users/Jesse Boyd/workspace/PuzzleGen/files/%%F" AS
)