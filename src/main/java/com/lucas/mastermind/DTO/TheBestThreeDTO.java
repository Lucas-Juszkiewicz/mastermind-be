package com.lucas.mastermind.DTO;


import lombok.Getter;

@Getter
public class TheBestThreeDTO {
     private UserDTO first;
     private UserDTO second;
     private UserDTO third;

     public TheBestThreeDTO(UserDTO first, UserDTO second, UserDTO third) {
          this.first = first;
          this.second = second;
          this.third = third;
     }

     public void setFirst(UserDTO first) {
          this.first = first;
     }

     public void setSecond(UserDTO second) {
          this.second = second;
     }

     public void setThird(UserDTO third) {
          this.third = third;
     }
}
