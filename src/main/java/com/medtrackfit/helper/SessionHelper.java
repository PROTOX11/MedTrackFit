package com.medtrackfit.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {
    public static void removeMessage() {
        
        try {
            System.out.println("removing message from session");
    
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            
            if (attributes != null) {  // Prevents NullPointerException
                HttpSession session = attributes.getRequest().getSession();
                session.removeAttribute("message");
            } else {
                System.out.println("Request attributes are null. Cannot remove session message.");
            }
        } catch (Exception e) {
            System.out.println("Error in session helper: " + e.getMessage());
        }
    }
}
