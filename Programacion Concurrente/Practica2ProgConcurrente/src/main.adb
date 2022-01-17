-- Practica 2 Programacion concurrente
-- Se pide programar una simulacion del problema de los babuinos viageros con
-- objetos protegidos de ada. Se tiene 2 grupos de babuinos en cada lado de un
-- desfiladero y en este desfiladero hay una cuerda para cruzar de un lado al
-- otro. Se pide programar el funcionamiento de la cuerda teniendo en cuenta
-- que en la cuerda no puede haber mas de 3 babuinos al mismo tiempo y no pueden
-- haber babuinos del norte y del sur cruzando la cuerda al mismo tiempo.

-- Autor: Victor Manuel Blanes Castro
-- Link video:
with Ada.Text_IO; use Ada.Text_IO;
with cuerda; use cuerda;
with ada.Numerics.Discrete_Random;

procedure main is
   NUM_BABUINOS: constant integer := 10;
   NUM_LOOPS: constant integer := 3;

   S: Monitor(3);

   -- randoNum
   -- Devuelve un numero aleatorio entre 1 y 250
   function randomNum return integer is
      type randRange is range 1..250;
      package Rand_Int is new ada.Numerics.Discrete_Random(randRange);
      use Rand_int;
      gen: Generator;
      num: randRange;
      begin
         reset(gen);
         num := random(gen);
         return Integer(num);
   end randomNum;

   task type tarea is
      entry Start(Idx: in integer; estc: in estado_cuerda);
   end tarea;

   -- Tarea que ejecutara cada nuevo hilo, se entra y sale de la cuerda (Seccion critica)
   -- NUM_LOOPS veces, se añade un tiempo de espera variable para impulsar el intercalado
   -- entre hilos.
   task body tarea is
      My_Idx : integer;
      My_estc : estado_cuerda;
      velocidad: Integer;
      success: Boolean;
      begin
      accept Start (Idx: in integer; estc: in estado_cuerda) do
         My_Idx := Idx;
         My_estc := estc;
         velocidad := (250 + randomNum) / 1000;
      end Start;
      Put_Line("Hola, soy el babuino" & Integer'Image(My_Idx) & " del "
               & estado_cuerda'Image(My_estc)& ".");
      for i in 1..NUM_LOOPS loop
         success := false;
         while not success loop
            s.entraCuerda(My_estc, success, My_Idx);
         end loop;
         delay Duration(velocidad);
         s.saleCuerda(My_estc, My_Idx);
         delay Duration(velocidad * 2);
         Put_Line("Babuino " & estado_cuerda'Image(My_estc) & Integer'Image(My_Idx)
                  & " hace la vuelta" & Integer'Image(i) & " de" & Integer'Image(NUM_LOOPS) & ".");
      end loop;
      Put_Line("Babuino " & estado_cuerda'Image(My_estc) & Integer'Image(My_Idx)
                  & " ha finalizado su ruta!!!");
   end tarea;

   type babuinos is array (1.. NUM_BABUINOS) of tarea;
   t: babuinos;

begin
      for Idx in 1.. NUM_BABUINOS loop
         if idx mod 2 = 1 then
            t(Idx).Start(Idx, estado_cuerda'(Norte));
         else
            t(Idx).Start(Idx, estado_cuerda'(Sur));
         end if;
      end loop;
end main;
