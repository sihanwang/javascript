function populate(sourceString) {

    var myList=document.getElementById(sourceString);

    if (myList.innerHTML===null || myList.innerHTML === "") {
        var sourceObject=eval(sourceString);

        var obj=new sourceObject();
    
        var options='';
    
        for (var i=0; i<obj.list.length; i++) {
            options += '<option value="' + obj.list[i]+'" />';
        }
    
        myList.innerHTML=options;
    }

}

class language {
    constructor() {
        this.list = ["java", "python", "javascript", "c", "angular"];
    }
}

class degree {
    constructor() {
        this.list = ["小学", "中学", "大学", "硕士", "博士"];
    }
}
