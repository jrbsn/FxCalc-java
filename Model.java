public class Model {

    static float[] getShapeParams(String shape) {
      float params[] = new float[3]; // cd, avt, cfc
      if (shape == "toroidal") {
        params[0] = 2.2f;
        params[1] = 2f; // I believe I sadly have to make this integer a float...
        params[2] = 8.8f;
      }
      else if (shape == "parabolic") {

      }
      return params;
    }
    static float[] preliminaryCalcs(float[] shapeParams, int diameter, int vls) {
      // will return [area, cds, tf]
      float area = (float) (Math.pow((diameter / 2), 2) * 3.1415926);
      float cds = area * shapeParams[0];
      float tf = (diameter * shapeParams[2]) / vls;
      float[] calcs = {area, cds, tf};
      return calcs;
    }
    static float[] inflationCurve(float[] data, float[] prelCalcs, float aird, float[] shapeParams) {
      // calculates drag for current step using inflation curve method
      float q = (float) (0.5 * aird * Math.pow(data[2], 2));
      float curve = (float) Math.pow((data[0] / prelCalcs[2]), shapeParams[1]);
      data[3] = q * prelCalcs[1] * curve;
      return data;
    }
    static void apparentMass() {

    }
    static float[] simulationStep(float[] data, int weight, float mass, float deltaT) {
      data[1] = (data[3] - weight) / mass; // acceleration
      data[2] += data[1] * deltaT; // velocity
      data[0] += deltaT;
      return data;
    }
    // main method just compiles all of the individual methods into a simulation
    public static void main(String[] args) {
      System.out.println("FxCalc");
      // Collect Inputs
      int weight = 100;
      float mass = (float) (weight / 32.174);
      float deltaT = 0.01f;
      int diameter = 10;
      int vls = 100;
      float aird = (float) 0.0023;
      float[] shapeParams = getShapeParams("toroidal"); // cd, avt, cfc
      float[] preCalcs = preliminaryCalcs(shapeParams, diameter, vls); // area, cds, tf

      // Simulation
      float[] data = {0, 0, (-1 * vls), weight}; // t, a, v, d
      int fx = 0;
      while (data[0] <= preCalcs[2]) { // while t <= tf
        data = inflationCurve(data, preCalcs, aird, shapeParams); // this whole thing didn't work out like I planned... combine into next
        data = simulationStep(data, weight, mass, deltaT);
        // store Fx
        if (data[3] >= fx) {fx = (int) data[3];}
      }
      System.out.println("Opening Force: " + fx + " lbs");
    }
  }