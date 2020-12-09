package com.example.Dtu_connect;

import java.io.Serializable;

public class Project  implements Serializable {

    String name;
    String url;
    String discription;

    Project(String name,String url,String discription)
    {
        this.discription=discription;
        this.name=name;
        this.url=url;
    }

}
