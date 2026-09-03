package com.example.legal_meterology.certificate;

import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class CertificateGenerator {

    public String generate(
            CertificateData data,
            String pdfPath,
            String qrPath) throws Exception {

        Document document = new Document(
                PageSize.A4,
                45,
                45,
                35,
                35
        );

        PdfWriter.getInstance(
                document,
                new FileOutputStream(pdfPath)
        );

        document.open();

        Font normalFont =
                new Font(Font.HELVETICA, 10, Font.NORMAL);

        Font smallFont =
                new Font(Font.HELVETICA, 8, Font.NORMAL);

        Font headingFont =
                new Font(Font.HELVETICA, 15, Font.BOLD);

        Font subHeadingFont =
                new Font(Font.HELVETICA, 11, Font.BOLD);

        /*
         * GOVERNMENT HEADER
         */

        Paragraph government = new Paragraph(
                "Government of India",
                subHeadingFont
        );

        government.setAlignment(Element.ALIGN_CENTER);
        document.add(government);

        Paragraph ministry = new Paragraph(
                "Ministry of Consumer Affairs, Food and Public Distribution",
                smallFont
        );

        ministry.setAlignment(Element.ALIGN_CENTER);
        document.add(ministry);

        Paragraph department = new Paragraph(
                "Department of Consumer Affairs",
                smallFont
        );

        department.setAlignment(Element.ALIGN_CENTER);
        document.add(department);

        Paragraph unit = new Paragraph(
                "Weights and Measures Unit",
                smallFont
        );

        unit.setAlignment(Element.ALIGN_CENTER);
        document.add(unit);

        /*
         * TITLE
         */

        Paragraph title = new Paragraph(
                "Certificate of Registration of Importer of Weights and Measures",
                headingFont
        );

        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(12);
        document.add(title);

        Paragraph section = new Paragraph(
                "[Under Section 19 of the Legal Metrology Act, 2009]",
                normalFont
        );

        section.setAlignment(Element.ALIGN_CENTER);
        document.add(section);

        /*
         * FILE NUMBER AND DATE
         */

        Paragraph fileInfo = new Paragraph(
                "F.No. " + safe(data.getFileNumber())
                        + "                         "
                        + "Dated- " + safe(data.getIssueDate()),
                normalFont
        );

        fileInfo.setSpacingBefore(15);
        document.add(fileInfo);

        /*
         * CERTIFY
         */

        Paragraph certify = new Paragraph();

        certify.setSpacingBefore(15);
        certify.setLeading(16);

        certify.add(
                new com.lowagie.text.Chunk(
                        "CERTIFY That ",
                        subHeadingFont
                )
        );

        certify.add(
                new com.lowagie.text.Chunk(
                        safe(data.getFirmName()),
                        subHeadingFont
                )
        );

        certify.add(
                new com.lowagie.text.Chunk(
                        "\n" + safe(data.getFirmAddress())
                                + "\nhave been registered as Importer of the following "
                                + "items of Weights and Measures: "
                                + safe(data.getImportedItems()),
                        normalFont
                )
        );

        document.add(certify);

        /*
         * REGISTRATION NUMBER
         */

        Paragraph registration = new Paragraph();

        registration.setSpacingBefore(15);

        registration.add(
                new com.lowagie.text.Chunk(
                        "Registration No: ",
                        subHeadingFont
                )
        );

        registration.add(
                new com.lowagie.text.Chunk(
                        safe(data.getRegistrationNumber()),
                        normalFont
                )
        );

        registration.add(
                new com.lowagie.text.Chunk(
                        "\nValid upto: ",
                        subHeadingFont
                )
        );

        registration.add(
                new com.lowagie.text.Chunk(
                        safe(data.getValidUpto()),
                        normalFont
                )
        );

        document.add(registration);

        /*
         * QR CODE
         */

        Image qr = Image.getInstance(qrPath);

        qr.scaleAbsolute(110, 110);
        qr.setAlignment(Element.ALIGN_CENTER);
        qr.setSpacingBefore(15);

        document.add(qr);

        Paragraph verification = new Paragraph(
                "Scan QR code to verify certificate",
                smallFont
        );

        verification.setAlignment(Element.ALIGN_CENTER);
        document.add(verification);

        /*
         * ISSUING AUTHORITY
         */

        Paragraph authority = new Paragraph();

        authority.setAlignment(Element.ALIGN_RIGHT);
        authority.setSpacingBefore(15);

        authority.add(
                new com.lowagie.text.Chunk(
                        safe(data.getIssuingAuthority()) + "\n",
                        normalFont
                )
        );

        authority.add(
                new com.lowagie.text.Chunk(
                        safe(data.getDesignation()) + "\n",
                        normalFont
                )
        );

        authority.add(
                new com.lowagie.text.Chunk(
                        "Tel: " + safe(data.getTelephone()) + "\n",
                        smallFont
                )
        );

        authority.add(
                new com.lowagie.text.Chunk(
                        "Email: " + safe(data.getEmail()),
                        smallFont
                )
        );

        document.add(authority);

        /*
         * NOTES
         */

        Paragraph notes = new Paragraph();

        notes.setSpacingBefore(15);

        notes.add(
                new com.lowagie.text.Chunk(
                        "Note:",
                        subHeadingFont
                )
        );

        notes.add(
                new com.lowagie.text.Chunk(
                        "\n1. No non-standard weight or measure should "
                                + "be imported without prior permission of "
                                + "the Central Government as required under "
                                + "Section 19 of the Legal Metrology Act, 2009.",
                        smallFont
                )
        );

        notes.add(
                new com.lowagie.text.Chunk(
                        "\n2. The registration does not necessarily constitute "
                                + "acceptance or recognition by the Government "
                                + "of the facts stated in the application.",
                        smallFont
                )
        );

        notes.add(
                new com.lowagie.text.Chunk(
                        "\n3. If the firm desires to suspend its activities, "
                                + "it should be informed immediately.",
                        smallFont
                )
        );

        notes.add(
                new com.lowagie.text.Chunk(
                        "\n4. The holder of the certificate should ensure "
                                + "that imported weights or measures have "
                                + "the model approved by the Central Government "
                                + "after import and before use or sale.",
                        smallFont
                )
        );

        document.add(notes);

        /*
         * CLOSE PDF
         */

        document.close();

        return pdfPath;
    }

    private String safe(String value) {

        if (value == null || value.isBlank()) {
            return "";
        }

        return value;
    }
}