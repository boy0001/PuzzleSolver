for %%F in ("files/*.txt") do (
   java com.boydti.puzzle.Main "files/%%F" %1
)