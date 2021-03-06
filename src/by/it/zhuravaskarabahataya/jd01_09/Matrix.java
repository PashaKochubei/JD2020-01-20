package by.it.zhuravaskarabahataya.jd01_09;
//почему copyOF на двумерки не срабатывает
//Настя, перепиши конструкторы и нормально скопируй

//В конструкторе лучше не вызывать методы,но этот получился монстром каким-то без них

class Matrix extends Var implements Operation {
    private double[][] values;

    public double[][] getValues() {
        return values;
    }

    public Matrix(double[][] values) {
        this.values = new double[values.length][0];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = new double[values[i].length];
            System.arraycopy(values[i], 0, this.values[i], 0, values[i].length);
        }
    }

    public Matrix(Matrix matrix) {
        this(matrix.values);
    }

    public Matrix(String strMatrix) {
        strMatrix = strMatrix.trim().replace(" ", "");
        String[] rows = strMatrix.split("},");
        for (int i = 0; i < rows.length; i++) {
            rows[i] = rows[i].replace("{", "").replace("}", "");
        }
        values = new double[rows.length][0];
        for (int i = 0; i < rows.length; i++) {
            String[] strArray = rows[i].split(",");
            values[i] = new double[strArray.length];
            for (int j = 0; j < strArray.length; j++) {
                values[i][j] = Double.parseDouble(strArray[j]);
            }
        }
//    Мои прошлые костыли:
//        StringBuilder sb = new StringBuilder(strMatrix);
//        sb.deleteCharAt(sb.length() - 1);
//        sb.deleteCharAt(0);
//        Pattern pattern = Pattern.compile("\\{\\s*[\\d,\\s]+}");
//        Matcher matcher = pattern.matcher(sb);
//        int counter = 0;
//        while (matcher.find()) {
//            counter++;
//        }
//        double[][] matrix = new double[counter][counter];
//        String[] strArray = new String[counter];
//        Pattern pattern1 = Pattern.compile("\\{\\s*[\\d,\\s]+}");
//        Matcher matcher1 = pattern1.matcher(sb);
//        int index = 0;
//        while (matcher1.find()) {
//            strArray[index++] = matcher1.group();
//        }
//        for (int i = 0; i < strArray.length; i++) {
//            StringBuilder sb2 = new StringBuilder(strArray[i]);
//            sb2.deleteCharAt(0);
//            sb2.deleteCharAt(sb2.length() - 1);
//            String[] strArr = sb2.toString().split(",");
//            for (int a = 0; a < strArr.length; a++) {
//                matrix[i][a] = Double.parseDouble(strArr[a]);
//            }
//        }
//        this.values = matrix;
    }

        @Override
    public Var add(Var other) {
        if (other instanceof Scalar) {
            double[][] result = new double[values.length][values[0].length];
            double s = ((Scalar) other).getValue();
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result.length; j++) {
                    result[i][j] = values[i][j] + s;
                }
            }
            return new Matrix(result);
        } else if (other instanceof Matrix) {
            double[][] first = this.values;
            double[][] second = ((Matrix) other).getValues();
            double[][] result = new double[first.length][first[0].length];
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result.length; j++) {
                    result[i][j] = first[i][j] + second[i][j];
                }
            }
            return new Matrix(result);
        }

        return super.add(other);
    }
    // написала для матрицы и скаляра, матрицы и матрицы. М + В = ??


    @Override
    public Var sub(Var other) {
        if (other instanceof Scalar) {
            double[][] result = new double[values.length][values[0].length];
            double s = ((Scalar) other).getValue();
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result.length; j++) {
                    result[i][j] = values[i][j] - s;
                }
            }
            return new Matrix(result);
        }
        if (other instanceof Matrix) {
            double[][] first = this.values;
            double[][] second = ((Matrix) other).getValues();
            double[][] result = new double[first.length][first[0].length];
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result.length; j++) {
                    result[i][j] = first[i][j] - second[i][j];
                }
            }
            return new Matrix(result);
        }
        return super.sub(other);
    }

    @Override
    public Var mul(Var other) {
        if (other instanceof Scalar) {
            double[][] result = new double[values.length][values[0].length];
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result.length; j++) {
                    result[i][j] = values[i][j] * ((Scalar) other).getValue();
                }
            }
            return new Matrix(result);
        } else if (other instanceof Vector) {
            double[] result = new double[((Vector) other).getValues().length];
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result.length; j++) {
                    result[i] += this.values[i][j] * ((Vector) other).getValues()[j];
                }
            }
            return new Vector(result);
        } else if (other instanceof Matrix && this.values.length == ((Matrix) other).getValues()[0].length) {
            double[][] first = this.values;
            double[][] second = ((Matrix) other).getValues();
            double[][] result = new double[this.values.length][((Matrix) other).getValues()[0].length];
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[0].length; j++) {
                    for (int r = 0; r < result.length; r++) {
                        result[i][r] += first[i][j] * second[j][r];
                    }
                }
            }
            return new Matrix(result);
        }
        return super.mul(other);
    }

    @Override
    public Var div(Var other) {
        if (other instanceof Scalar) {
            double[][] result = new double[values.length][values[0].length];
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result.length; j++) {
                    result[i][j] = values[i][j] / ((Scalar) other).getValue();
                }
            }
            return new Matrix(result);
        }
        return super.div(other);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("{");
        String rowsDelimiter = "";
        for (double[] value : values) {
            result.append(rowsDelimiter);
            String delimiter = "";
            result.append("{");
            for (int j = 0; j < values.length; j++) {
                result.append(delimiter).append(value[j]);
                delimiter = ", ";
            }
            rowsDelimiter = ", ";
            result.append("}");
        }
        result.append("}");
        return result.toString();
    }
}
