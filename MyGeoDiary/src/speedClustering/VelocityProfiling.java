package speedClustering;

public enum VelocityProfiling {
    stop (0),
    walk (1),
    jogging (2),
    bike (3),
    car (4),
    plane (5),
    teleport (6),
    error (7);

    private int velo;
    VelocityProfiling(int velo){
        this.velo=velo;
    }
    public int getVelo(){
        return velo;
    }
    public void setVelo(int velo){
        this.velo=velo;
    }

    public int veloProf(double velocity){
        if (velocity <= 1.00) {
            velo=0;
        } else if (velocity <= 5.50) {
            velo=1;
        } else if (velocity <= 10.00) {
            velo=2;
        } else if (velocity <=25.00){
            velo=3;
        } else if (velocity <=200.00){
            velo=4;
        } else if (velocity <= 2000.00){
            velo=5;
        } else if (velocity <=5000.00){
            velo=6;
        } else {
            System.out.println("Error! The velocity couldnt be meassured");
            velo=7;
        }
         return velo;
    }
}