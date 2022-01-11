with Ada.Text_IO; use Ada.Text_IO;
package body cuerda is

   protected body Monitor is
      
      entry entraCuerda(babuino: in  estado_cuerda; success: out Boolean; idx: in Integer) 
        when En_Cuerda < Cap_Maxima is
      begin
         if(babuino = est or est = estado_cuerda'(Libre)) then
            Put_Line("      Babuino " & estado_cuerda'Image(babuino) & Integer'Image(idx) 
                     & " esta en la cuerda.");
            En_Cuerda := En_Cuerda + 1;
            est := estado_cuerda'(babuino);
            Put_Line("            +++++ En la cuerda hay" & Integer'Image(En_Cuerda) 
                     & " babuinos del " & estado_cuerda'Image(est) & ". +++++");
            success := True;
         else
            success := False;
         end if;
      end entraCuerda;

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
   end Monitor;

end cuerda;
