package uob.oop;

public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {
        doubElements = _elements;

    }

    public double getElementatIndex(int _index) {
        if (_index >= doubElements.length) return -1;
        else{
            return doubElements[_index];
        }

    }

    public void setElementatIndex(double _value, int _index) {
        if (_index >= doubElements.length) doubElements[doubElements.length - 1] = _value;
        else doubElements[_index] = _value;


    }

    public double[] getAllElements() {

        return doubElements; 
    }

    public int getVectorSize() {
        return doubElements.length; 
    }

    public Vector reSize(int _size) {
        if (_size <= 0 || _size == doubElements.length) return this;
        else if(_size < doubElements.length) {
            double[] newElements = new double[_size];
            for (int i = 0; i<_size; i++) {
                newElements[i] = doubElements[i];
            }
            return new Vector(newElements);
            }
        else{
            double[] newElements = new double[_size];
            for (int i = 0; i<_size; i++) {
                if (i > doubElements.length - 1) newElements[i] = -1.0;
                else newElements[i] = doubElements[i];
            }
            return new Vector(newElements);
        }

    }

    public Vector add(Vector _v) {
        //firstly, if _v was bigger than double elements, we would use the resize
        //method on this, then we would get all its new elements and assign it to
        //doubleElements.
        if (_v.getVectorSize() > doubElements.length) doubElements = this.reSize(_v.getVectorSize()).getAllElements();
        else if(_v.getVectorSize() < doubElements.length) _v = _v.reSize(doubElements.length);
        for (int i = 0; i < doubElements.length; i++) {
            doubElements[i] = doubElements[i] + _v.getElementatIndex(i);
        }
        return this;

    }

    public Vector subtraction(Vector _v) {
        if (_v.getVectorSize() > doubElements.length) doubElements = this.reSize(_v.getVectorSize()).getAllElements();
        else if(_v.getVectorSize() < doubElements.length) _v = _v.reSize(doubElements.length);
        for (int i = 0; i < doubElements.length; i++) {
            doubElements[i] = doubElements[i] - _v.getElementatIndex(i);
        }

        return this; 
    }

    public double dotProduct(Vector _v) {
        if (_v.getVectorSize() > doubElements.length) doubElements = this.reSize(_v.getVectorSize()).getAllElements();
        else if(_v.getVectorSize() < doubElements.length) _v = _v.reSize(doubElements.length);
        double total = 0;
        for (int i = 0; i < doubElements.length; i++) {
            total = total + (doubElements[i]*_v.getElementatIndex(i));
        }
        return total;
    }

    //my own method to make it easier
    public double squareRootOfSumSquare() {
        double total = 0;
        for (int i = 0; i<doubElements.length; i++) {
            total = total + (doubElements[i]*doubElements[i]);
        }
        total = Math.sqrt(total);
        return total;
    }



    public double cosineSimilarity(Vector _v) {
        if (_v.getVectorSize() > doubElements.length) doubElements = this.reSize(_v.getVectorSize()).getAllElements();
        else if(_v.getVectorSize() < doubElements.length) _v = _v.reSize(doubElements.length);
        double dot = this.dotProduct(_v);
        //I made this method to make it easier, it is above the current method we are in
        double _Vsum = _v.squareRootOfSumSquare();
        double thisSum = this.squareRootOfSumSquare();
        return dot/(_Vsum*thisSum);
    }

    @Override
    public boolean equals(Object _obj) {
        Vector v = (Vector) _obj;
        boolean boolEquals = true;

        if (this.getVectorSize() != v.getVectorSize())
            return false;

        for (int i = 0; i < this.getVectorSize(); i++) {
            if (this.getElementatIndex(i) != v.getElementatIndex(i)) {
                boolEquals = false;
                break;
            }
        }
        return boolEquals;
    }

    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < this.getVectorSize(); i++) {
            mySB.append(String.format("%.5f", doubElements[i])).append(",");
        }
        mySB.delete(mySB.length() - 1, mySB.length());
        return mySB.toString();
    }
}
