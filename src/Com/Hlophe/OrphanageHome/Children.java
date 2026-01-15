package Com.Hlophe.OrphanageHome;

public class Children {
    private int id;
    private String name;
    private String surname;
    private int age;
    private String race;
    
    public Children() {
    }
    
    public Children(String name, String surname, int age, String race) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.race = race;
    }
    
    public Children(int id, String name, String surname, int age, String race) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.race = race;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getRace() {
        return race;
    }
    
    public void setRace(String race) {
        this.race = race;
    }
    
    public String toJson() {
        String _name = name != null ? name : "";
        String _surname = surname != null ? surname : "";
        String _race = race != null ? race : "No description available";
        
        return String.format(
            "{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\",\"age\":%d,\"description\":\"%s\"}",
            id, 
            escapeJson(_name), 
            escapeJson(_surname), 
            age, 
            escapeJson(_race)
        );
    }
    
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}