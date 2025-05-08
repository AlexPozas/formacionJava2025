package com.edisa.formacion.mayo2025;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class Recursos {

    @GET
    @Path("/saludo")
    public Response saludar(@QueryParam("nombre") String nombre,
                            @QueryParam("apellido") String apellido,
                            @QueryParam("edad") int edad) {

        return Response.status(404).build();
    }

    @POST
    @Path("/saludo/post")
    public String saludar_post(@QueryParam("nombre") String nombre,
                               @QueryParam("apellido") String apellido,
                               @QueryParam("edad") int edad) {

        return "Hola desde el metodo POST, " + nombre + " " + apellido + ". Tienes " + edad + " años.";
    }

    @GET
    @Path("/codabar/generar")
    public Response generarCodigoBarras(@QueryParam("texto") String texto,
                                        @QueryParam("direccion") String direccion) {
        if (texto == null || direccion == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Parámetros 'texto' y 'direccion' son obligatorios\"}")
                    .build();
        }

        try {
            // Usamos CODABAR como formato fijo
            BarcodeFormat formato = BarcodeFormat.CODABAR;

            int width = 400;
            int height = 150;

            BitMatrix bitMatrix = new com.google.zxing.MultiFormatWriter()
                    .encode(texto, formato, width, height);

            Path path = (Path) Paths.get(direccion);

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", (java.nio.file.Path) path);

            return Response.ok("{\"message\":\"Código de barras guardado en: " + ((java.nio.file.Path) path).toAbsolutePath() + "\"}")
                    .build();

        } catch (WriterException | IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/codabar/generar2")
    @Produces("image/png")  // Cambiado de "application/octet-stream" a "image/png"
    public Response generarCodigoBarras2(@QueryParam("texto") String texto,
                                         @QueryParam("formato") String formato) {
        if (texto == null || formato == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Parámetros 'texto' y 'formato' son obligatorios")
                    .build();
        }

        try {
            // Validar formato
            BarcodeFormat barcodeFormat;
            try {
                barcodeFormat = BarcodeFormat.valueOf(formato.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Formato no válido: " + formato)
                        .build();
            }

            int width = 400;
            int height = 150;

            BitMatrix bitMatrix = new com.google.zxing.MultiFormatWriter()
                    .encode(texto, barcodeFormat, width, height);

            // Convertir imagen a array de bytes (PNG en memoria)
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            return Response.ok(imageBytes)
                    .type("image/png") // Content-Type correcto para mostrar la imagen
                    .build();

        } catch (WriterException | IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error generando el código de barras: " + e.getMessage())
                    .build();
        }
    }



}