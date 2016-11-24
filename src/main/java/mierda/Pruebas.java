package mierda;

import com.trelloapp.dto.assembler.EjemploDTOMapper;

public class Pruebas {
    public static void main(String[] args) {
        /*
        String username = "pene@pene.pene";
        String name = "Mr. Pene";
        String password = "toletillo";
        boolean isMyself = true;

        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(password);

        //UserDTO dto = UserDTOAssembler.INSTANCE.toDTO(user, isMyself);
        UserDTOAssembler ass = UserDTOAssembler.INSTANCE;
        UserDTO dto = ass.toDTO(user, isMyself);
        System.out.println(dto.getName());
        */

        Ejemplo e = new Ejemplo();
        e.setA1("a");
        e.setA2("b");

        EjemploDTOMapper ass = EjemploDTOMapper.INSTANCE;
        EjemploDTO a = ass.toDTO(e);
        a.getA1();
    }
}
