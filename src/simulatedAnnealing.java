import jm.JMC;
import jm.util.*;
import jm.music.data.*;
//import java.util.Vector;
@SuppressWarnings("unused")
public final class simulatedAnnealing implements JMC {
    private static Part randomPart = new Part("Piano", PIANO, 1);
    private static Part part2= new Part("Drums", 0, 9);
    private static Score score = new Score("AlgoMusic", 84);
    private static Phrase phr = new Phrase();
    private static int compDur = 8;
    private static int[] pitchSelection = {60, 62, 64, 65, 67, 69, 71, 72}; //C Major Scale;
    private static double prob = 0;

    public static void main(String[] args){
        // Simulated Annealing Algorithm
        randomPart = createRandomPart();
        int numberOfIterations = 500;
        int fit = calculateMusic(randomPart);
        for(int i = 0; i < numberOfIterations; i++){
            Part mutatedPart = createRandomPart();
            int fit2 = calculateMusic(mutatedPart);
            prob = PR(fit, fit2);
            double ranValue = Math.random();
            if(ranValue > prob) {
                fit = fit2;
            }
        }
        //Make the drum parts
        for(int j = 0; j < compDur / 2; j++) {
            CPhrase drums = MakeParts.drumPattern();
            part2.addCPhrase(drums);
        }

        score.addPart(randomPart);
        //score.addPart(part2);

        // Shows score in a new window.
        View.show(score);

        //Write a MIDI file to disk
        Write.midi(score, "simulatedAnnealing.mid");
        //View.print(s);

    }

    private static Part createRandomPart(){
        //Choose Scale
        int pitch;
        Part tempPart = new Part();

        int noteNumb = 1;
        //create the initial bass phrase
        for (int i = 0; i < noteNumb; i++) {
            pitch = pitchSelection[(int) (Math.random() * pitchSelection.length)];
            Note note = new Note(pitch, SQ, (int) (Math.random() * 70 + 50));
            phr.addNote(note);
        }

        //the main Bass mutate loop
        int x = (int) (Math.random() * (16-1))+1;
        for (int i = 0; i < compDur; i++) {
            //add the new phrase to the part
            tempPart.addPhrase(phr.copy());

            //mutate random amount of note
            for(int j = 0; j < x; j++){
                Note n = phr.getNote((int) (Math.random() * noteNumb));
                n.setPitch(pitchSelection[(int) (Math.random() * pitchSelection.length)]);
            }

        }

        //return random score
        return tempPart;
    }

    private static int calculateMusic(Part tempPart){
        int p = 0;
        int n = 0;
        int noteVal;
        int fitness;
        int finalFitnessLevel = 0;
        Phrase tempPhrase = tempPart.getPhrase(p);
        Note tempNote = tempPhrase.getNote(n);
        //System.out.println(tempPart.length());
        for(int i = 0; i < tempPart.length(); i++) {
            noteVal = tempNote.getPitch();
            if (noteVal != pitchSelection[i]) {
                fitness = Math.abs(noteVal - pitchSelection[i]);
            } else {
                fitness = 0;
            }
            finalFitnessLevel = finalFitnessLevel + fitness;

        }
        return finalFitnessLevel;
    }

    private static double PR(int fit, int fit2){
        prob = (float)fit / (fit + fit2);
        return prob;

    }

    private static Part justMutateNotes(Part tempPart){
        int p = 0;
        int x = 8;
        int noteNumb = 1;
        Phrase tempPhrase = tempPart.getPhrase(p);

        for(int j = 0; j < x; j++){
            Note tempNote = tempPhrase.getNote((int) (Math.random() * noteNumb));
            tempNote.setPitch(pitchSelection[(int) (Math.random() * pitchSelection.length)]);
        }
        return tempPart;
    }
}