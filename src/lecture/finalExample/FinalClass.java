package lecture.finalExample;

public final class FinalClass {
    int finalData = 10;
    FinalClass(int finalData) {
        this.finalData = finalData;
    }

    public int getFinalData() {
        return finalData;
    }
    
    public void setFinalData(int finalData) {
        this.finalData = finalData;
    }


    // Cannot inherit from final 'lecture.finalExample.FinalClass'
//    private class ExtendClass extends FinalClass {
//        ExtendClass(int finalData) {
//            super(finalData);
//        }
//    }
}

// Cannot inherit from final 'lecture.finalExample.FinalClass'
// class FinalClass2 extends FinalClass {
//    int finalData = 10;
//}
