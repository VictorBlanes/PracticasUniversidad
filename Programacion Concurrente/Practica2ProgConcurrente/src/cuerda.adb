with Ada.Text_IO; use Ada.Text_IO;
package body cuerda is

   protected body Monitor is
      -- entry entraCuerda
      -- Controla el acceso a la seccion critica, si hay espacio en la cuerda se entra
      -- y despues se controla si se puede usar la cuerda (No hay babuinos en
      -- direccion contraria, si se puede usar se continua aumentando en 1 el nombre 
      -- babuinos en la cuerda y se pone que la cuerda esta siendo usada por los 
      -- babuinos de esa direccion, si no se sale inmediatamente. 
      entry entraCuerda(for babuino in estado_cuerda)(idx: in Integer)
        when En_Cuerda < Cap_Maxima and usarCuerda(babuino) is
      begin
            Put_Line("      Babuino " & estado_cuerda'Image(babuino) & Integer'Image(idx) 
                     & " esta en la cuerda.");
            En_Cuerda := En_Cuerda + 1;
            est := estado_cuerda'(babuino);
            Put_Line("            +++++ En la cuerda hay" & Integer'Image(En_Cuerda) 
                     & " babuinos del " & estado_cuerda'Image(est) & ". +++++");
      end entraCuerda;
      
      --entry saleCuerda
      -- Controla el acceso a la seccion critica, se resta en 1 el numero de 
      -- babuinos que hay en la cuerda y si este numero pasa a ser 0 se pone la
      -- cuerda como libre.
      procedure saleCuerda(babuino: in  estado_cuerda; idx: in Integer) is
      begin
         Put_Line("      Babuino " & estado_cuerda'Image(babuino) & Integer'Image(idx) 
                  & " sale de la cuerda.");
         En_Cuerda := En_Cuerda - 1;
         Put_Line("            ----- En la cuerda hay" & Integer'Image(En_Cuerda) 
                  & " babuinos del " & estado_cuerda'Image(est) & ". -----");
         if (En_Cuerda = 0) then
            est := estado_cuerda'(Libre);
            Put_Line("            ***** CUERDA LIBRE *****");
         end if;
      end saleCuerda;
   
      function usarCuerda(babuino: in  estado_cuerda) return Boolean is
      begin
         return est = estado_cuerda'(Libre) or est = estado_cuerda'(babuino);
         end usarCuerda;
   end Monitor;

end cuerda;
