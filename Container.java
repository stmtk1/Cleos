class Container{
    public static void main(String args[]){
        new Container();
    }
    
    public Container(){
    	new CleosFrame(this);
    }
    
    public void addFrame(){
        new CleosFrame(this);
    }
}




