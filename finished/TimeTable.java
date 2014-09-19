import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalTime;


public class TimeTable {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String line = br.readLine();
        int num_entries = Integer.parseInt(line);
        
        TimeTable timeTable = new TimeTable();
        
        for (int i = 0; i < num_entries; i++) {
            line = br.readLine();
            String[] name_and_minutes = line.split(" ");
            String name = name_and_minutes[0];
            int minutes = Integer.parseInt(name_and_minutes[1]);
            timeTable.enter(name, minutes);
        }
        
        timeTable.build();
        
        System.out.println(timeTable);
    }

    private static final LocalTime START_TIME               = LocalTime.parse("10:00");
    private static final LocalTime END_TIME_LIMIT_FOR_LUNCH = LocalTime.parse("12:00");

    private static final int BREAK_LENGTH_IN_MINUTES       = 10;
    private static final int LUNCH_BREAK_LENGTH_IN_MINUTES = 60;

    private final List<Entry> entries = new ArrayList<Entry>();
    private boolean isLunchBreakAlreadyTaken;

    public void enter(final String name, final int minutes) {
        entries.add(new Entry(name, minutes));
    }
    
    public void build() {
        boolean isLunchBreakAlreadyTaken = false;
        
        LocalTime time = START_TIME;
        for (int i = 0, size = entries.size(); i < size; i++) {
            Entry entry = entries.get(i);
            entry.setStartTime(time);
            
            Entry nextEntry = i >= size - 1 ? null : entries.get(i + 1);
            int breakLengthInMinutes = getBreakLengthInMinutes(entry, nextEntry);
            time = entry.getEndTime().plusMinutes(breakLengthInMinutes);
        }
    }
    
    private int getBreakLengthInMinutes(final Entry entry, final Entry nextEntry) {
        return isToBreakForLunch(entry, nextEntry)
                    ? LUNCH_BREAK_LENGTH_IN_MINUTES
                    : BREAK_LENGTH_IN_MINUTES
                    ;
    }
    
    private boolean isToBreakForLunch(final Entry entry, final Entry nextEntry) {
        if (isLunchBreakAlreadyTaken || nextEntry == null) {
            return false;
        }
        boolean isToBreak = entry.getEndTime()
									.plusMinutes(BREAK_LENGTH_IN_MINUTES)
									.plusMinutes(nextEntry.minutes)
                            .isAfter(END_TIME_LIMIT_FOR_LUNCH);
        if (isToBreak) {
            isLunchBreakAlreadyTaken = true;
        }
        return isToBreak;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry entry : entries) {
            sb.append(entry + "\n");
        }
        return sb.toString();
    }
    
    private static class Entry {
        private final String name;
        private final int minutes;
        private LocalTime startTime;
        
        public Entry(final String name, final int minutes) {
            this.name = name;
            this.minutes = minutes;
        }

        public String toString() {
            if (startTime == null) {
                return String.format("%s: %d [min]", name, minutes);
            }
            return String.format("%s - %s %s", startTime, getEndTime(), name);
        }
        
        public int getMinutes() {
            return minutes;
        }
        
        public LocalTime getEndTime() {
            if (startTime == null) {
                return null;
            }
            return startTime.plusMinutes(minutes);
        }
        
        public void setStartTime(final LocalTime startTime) {
            this.startTime = startTime.plusNanos(0);
        }
    }
}
