package com.alan.devgitfinder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Stack;

@SpringBootTest
public class FindEndBracketTests {

    @Test
    public void context(){
        String s ="dependencies {\n" +
                "        annotationProcessor 'org.projectlombok:lombok'\n" +
                "        implementation 'org.projectlombok:lombok'\n" +
                "        testAnnotationProcessor 'org.projectlombok:lombok'\n" +
                "        testImplementation 'org.projectlombok:lombok'\n" +
                "\n" +
                "        testImplementation('org.springframework.boot:spring-boot-starter-test') {\n" +
                "            exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'\n" +
                "        }\n" +
                "    }";

        int index = s.indexOf("dependencies");
        int endBracketIndex = solution(s);

        System.out.println(s.substring(index,endBracketIndex));


    }

    public int solution (String s){
        int answer = 0;
        Stack<Character> stack = new Stack<>();

        for(int i=0;i<s.length();i++) {
            if(!stack.isEmpty() && s.charAt(i) == '}'){
                stack.pop();
                if(stack.isEmpty()) {
                    answer = i;
                    break;
                }
            } else if (s.charAt(i)=='{'){
                stack.push(s.charAt(i));
            }
        }

        return answer+1;
    }

}
